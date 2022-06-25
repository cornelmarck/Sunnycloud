package com.cornelmarck.sunnycloud.service;

import com.cornelmarck.sunnycloud.dto.SolaredgeDataPeriodDto;
import com.cornelmarck.sunnycloud.dto.SolaredgePowerDto;
import com.cornelmarck.sunnycloud.exception.SolaredgeApiException;
import com.cornelmarck.sunnycloud.model.Power;
import com.cornelmarck.sunnycloud.model.SolaredgeApiConfig;
import com.cornelmarck.sunnycloud.repository.PowerRepository;
import com.cornelmarck.sunnycloud.util.TimeRange;
import com.cornelmarck.sunnycloud.util.TimeUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SolaredgeApiService {
    private final Logger logger = LoggerFactory.getLogger(SolaredgeApiService.class);

    private final RestTemplate restTemplate;
    private final String solarEdgeEndpoint;
    private final ObjectMapper objectMapper;
    private final PowerRepository powerRepository;
    private final SiteService siteService;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final Duration maxPowerFetchPeriod = Duration.of(28, ChronoUnit.DAYS);

    public void updateSite(String siteId, SolaredgeApiConfig config) {
        logger.info("Updating siteId={}", siteId);
        TimeRange toUpdate = getRangeToUpdate(siteId, config.getExternalSiteId(), config.getApiKey());
        toUpdate.split(maxPowerFetchPeriod).forEach(x -> updateRange(x, siteId, config));
    }

    private void updateRange(TimeRange range, String siteId, SolaredgeApiConfig config) {
        ZoneId zoneId = siteService.getTimeZoneId(siteId);
        LocalDateTime from = TimeUtils.getLocalDateTime(range.getFrom(), zoneId);
        LocalDateTime to = TimeUtils.getLocalDateTime(range.getTo(), zoneId);
        logger.info("Updating time range from={} to={}", from.toString(), to.toString());

        try {
            getPowerBetween(config.getExternalSiteId(), config.getApiKey(), from, to).stream()
                    .map(x -> x.toPower(siteId, zoneId))
                    .collect(Collectors.toList())
                    .forEach(powerRepository::save);
        }
        catch (IOException e) {
            throw new SolaredgeApiException(e.getMessage());
        }
    }

    private TimeRange getRangeToUpdate(String siteId, String externalSiteId, String apiKey) {
        ZoneId zoneId = siteService.getTimeZoneId(siteId);
        Optional<Power> latest = powerRepository.findLatestBySiteId(siteId);
        if (latest.isEmpty()) {
            return new TimeRange(getDataPeriod(externalSiteId, apiKey).getStart(zoneId), Instant.now());
        }
        return new TimeRange(latest.get().getTimestamp(), Instant.now());
    }

    private List<SolaredgePowerDto> getPowerBetween(String externalSiteId, String apiKey, LocalDateTime from, LocalDateTime to)
            throws IOException {
        LocalDateTime adjustedStartTime = TimeUtils.roundUpToQuarter(from);
        logger.debug("Fetch power externalSiteId={} from={} to={}", externalSiteId, adjustedStartTime, to);

        String requestURL = getPowerURL(externalSiteId, apiKey, TimeUtils.roundUpToQuarter(from), to);
        ResponseEntity<JsonNode> entity = restTemplate.getForEntity(requestURL, JsonNode.class);
        logger.debug("Power fetch status code: {}", entity.getStatusCode());
        Objects.requireNonNull(entity.getBody());

        JsonNode powerListNode = entity.getBody().get("power").get("values");
        ObjectReader objectReader = objectMapper.readerFor(new TypeReference<List<SolaredgePowerDto>>(){});
        List<SolaredgePowerDto> outputPowerList = objectReader.readValue(powerListNode);
        logger.info("Collected {} power measurements externalSiteId={}", outputPowerList.size(), externalSiteId);
        return outputPowerList;
    }

    private String getPowerURL(String externalSiteId, String apiKey, LocalDateTime from, LocalDateTime to) {
        return solarEdgeEndpoint + "/site/" + externalSiteId + "/power.json"
                + "?api_key=" + apiKey + "&startTime=" + from.format(formatter) + "&endTime=" + to.format(formatter);
    }

    private SolaredgeDataPeriodDto getDataPeriod(String externalSiteId, String apiKey) {
        try {
            logger.debug("Fetch data period externalSiteId={}", externalSiteId);
            String requestURL = solarEdgeEndpoint + "/site/" + externalSiteId + "/dataPeriod.json?api_key=" + apiKey;
            ResponseEntity<JsonNode> entity = restTemplate.getForEntity(requestURL, JsonNode.class);
            logger.debug("Data period fetch status code: {}", entity.getStatusCode());
            Objects.requireNonNull(entity.getBody());
            return objectMapper.treeToValue(entity.getBody().get("dataPeriod"), SolaredgeDataPeriodDto.class);
        }
        catch (JsonProcessingException e) {
            throw new SolaredgeApiException(e.getMessage());
        }


    }
}
