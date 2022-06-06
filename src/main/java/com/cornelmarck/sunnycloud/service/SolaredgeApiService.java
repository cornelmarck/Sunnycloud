package com.cornelmarck.sunnycloud.service;

import com.cornelmarck.sunnycloud.dto.SolaredgeDataPeriodDto;
import com.cornelmarck.sunnycloud.dto.SolaredgePowerDto;
import com.cornelmarck.sunnycloud.exception.SolaredgeApiException;
import com.cornelmarck.sunnycloud.model.Power;
import com.cornelmarck.sunnycloud.repository.PowerRepository;
import com.cornelmarck.sunnycloud.util.TimeRange;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import lombok.RequiredArgsConstructor;
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
    private final RestTemplate restTemplate;
    private final String solarEdgeEndpoint;
    private final ObjectMapper objectMapper;
    private final PowerRepository powerRepository;
    private final SiteService siteService;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final Duration maxPowerFetchPeriod = Duration.of(28, ChronoUnit.DAYS);

    public void update(SolaredgeApiConfig syncApi) {
        Optional<Power> latest = powerRepository.findLatestBySiteId(syncApi.getSiteId());
        ZoneId zoneId = siteService.getTimeZoneId(syncApi.getSiteId());
        if (latest.isPresent()) {
            updateBetween(syncApi, latest.get().getTimestamp(), Instant.now());
        }
        else {
            try {
                Instant start = LocalDate.parse(getDataPeriod(syncApi).getStartDate()).atStartOfDay().atZone(zoneId).toInstant();
                updateBetween(syncApi, start, Instant.now());
            }
            catch (JsonProcessingException e) {
                throw new SolaredgeApiException(e.getMessage());
            }
        }
    }

    public void updateBetween(SolaredgeApiConfig syncApi, Instant from, Instant to) {
        TimeRange timeRange = new TimeRange(from, to);
        for (TimeRange range : timeRange.split(maxPowerFetchPeriod)) {
            try {
                List<Power> measurements = getPowerBetween(syncApi, range.getFrom(), range.getTo());
                powerRepository.batchSave(measurements);
            }
            catch (IOException e) {
                throw new SolaredgeApiException(e.getMessage());
            }
        }
    }

    private List<Power> getPowerBetween(SolaredgeApiConfig syncApi, Instant from, Instant to) throws IOException {
        ZoneId zoneId = siteService.getTimeZoneId(syncApi.getSiteId());
        String requestURL = getPowerURL(syncApi, getLocalDateTime(from, zoneId), getLocalDateTime(to, zoneId));
        ResponseEntity<JsonNode> entity = restTemplate.getForEntity(requestURL, JsonNode.class);
        Objects.requireNonNull(entity.getBody());
        JsonNode powerListNode = entity.getBody().get("power").get("values");
        ObjectReader objectReader = objectMapper.readerFor(new TypeReference<List<SolaredgePowerDto>>(){});
        List<SolaredgePowerDto> powerDtoList = objectReader.readValue(powerListNode);
        return powerDtoList.stream()
                .map(x -> x.toPower(syncApi.getSiteId(), zoneId))
                .collect(Collectors.toList());
    }

    private String getPowerURL(SolaredgeApiConfig syncApi, LocalDateTime from, LocalDateTime to) {
        return solarEdgeEndpoint + "/site/" + syncApi.getExternalSiteId() + "/power.json?"
                + "api_key=" + syncApi.getApiKey() + "&siteId=" + syncApi.getExternalSiteId()
                + "&startTime=" + from.format(formatter) + "&endTime=" + to.format(formatter);
    }

    private SolaredgeDataPeriodDto getDataPeriod(SolaredgeApiConfig syncApi) throws JsonProcessingException {
        String requestURL = solarEdgeEndpoint + "/site/" + syncApi.getExternalSiteId()
                + "/dataPeriod.json?api_key=" + syncApi.getApiKey();
        return objectMapper.readValue(requestURL, SolaredgeDataPeriodDto.class);
    }

    private LocalDateTime getLocalDateTime(Instant instant, ZoneId zoneId) {
        return instant.atZone(zoneId).toLocalDateTime();
    }
}
