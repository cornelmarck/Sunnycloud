package com.cornelmarck.sunnycloud.service;

import com.cornelmarck.sunnycloud.dto.SolaredgeDataPeriodDto;
import com.cornelmarck.sunnycloud.dto.SolaredgePowerDto;
import com.cornelmarck.sunnycloud.exception.SolaredgeApiException;
import com.cornelmarck.sunnycloud.model.Power;
import com.cornelmarck.sunnycloud.model.SolaredgeApiConfig;
import com.cornelmarck.sunnycloud.repository.PowerRepository;
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
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
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
    private static final Duration measurementPeriod = Duration.of(15, ChronoUnit.MINUTES);

    public void updateSite(String siteId, SolaredgeApiConfig config) {
        logger.info("Updating siteId={}", siteId);
        LocalDateTime start = getUpdateStartingPoint(siteId, config.getExternalSiteId(), config.getApiKey());
        LocalDateTime now = Instant.now().atZone(siteService.getTimeZoneId(siteId)).toLocalDateTime();

        while (Duration.between(start, now).compareTo(measurementPeriod) >= 0) {
            LocalDateTime end = Collections.min(List.of(now, start.plus(maxPowerFetchPeriod)));
            updateRange(siteId, start, end, config.getExternalSiteId(), config.getApiKey());
            start = getUpdateStartingPoint(siteId, config.getExternalSiteId(), config.getApiKey());
        }
    }

    private void updateRange(String siteId, LocalDateTime from, LocalDateTime to, String externalSiteId, String apiKey) {
        ZoneId zoneId = siteService.getTimeZoneId(siteId);
        logger.info("Updating time range from={} to={}", from.toString(), to.toString());

        try {
            getPowerBetween(externalSiteId, apiKey, from, to).stream()
                    .map(x -> x.toPower(siteId))
                    .collect(Collectors.toList())
                    .forEach(powerRepository::save);
        }
        catch (IOException e) {
            throw new SolaredgeApiException(e.getMessage());
        }
    }

    private LocalDateTime getUpdateStartingPoint(String siteId, String externalSiteId, String apiKey) {
        Optional<Power> latest = powerRepository.findLatestBySiteId(siteId);
        if (latest.isEmpty()) {
            return getDataPeriod(externalSiteId, apiKey).getStart().atStartOfDay();
        }
        return latest.get().getTimestamp();
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
            Objects.requireNonNull(entity.getBody());
            return objectMapper.treeToValue(entity.getBody().get("dataPeriod"), SolaredgeDataPeriodDto.class);
        }
        catch (JsonProcessingException e) {
            logger.error("Json processing error ");
            throw new SolaredgeApiException(e.getMessage());
        }
    }
}
