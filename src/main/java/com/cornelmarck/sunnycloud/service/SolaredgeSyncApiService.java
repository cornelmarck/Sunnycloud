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

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SolaredgeSyncApiService {
    private final RestTemplate restTemplate;
    private final String solarEdgeEndpoint;
    private final ObjectMapper objectMapper;
    private final PowerRepository powerRepository;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final Duration maxPowerFetchPeriod = Duration.of(28, ChronoUnit.DAYS);

    public void update(SolaredgeSyncApi syncApi) throws JsonProcessingException {
        Optional<Power> latest = powerRepository.findLatestBySiteId(syncApi.getSiteId());
        if (latest.isPresent()) {
            updateBetween(syncApi, latest.get().getTimestamp().plusSeconds(1), LocalDateTime.now());
        }
        else {
            LocalDateTime start = LocalDate.parse(getDataPeriod(syncApi).getStartDate()).atStartOfDay();
            updateBetween(syncApi, start, LocalDateTime.now());
        }
    }

    public void updateBetween(SolaredgeSyncApi syncApi, LocalDateTime from, LocalDateTime to) {
        TimeRange timeRange = new TimeRange(from, to);
        for (TimeRange range : timeRange.splitToNonEmpty(maxPowerFetchPeriod)) {
            List<Power> measurements = getPowerBetween(syncApi, range.getFrom(), range.getTo());
            powerRepository.batchSave(measurements);
        }
    }

    private List<Power> getPowerBetween(SolaredgeSyncApi syncApi, LocalDateTime from, LocalDateTime to) {
        try {
            String requestURL = getPowerURL(syncApi, from, to);
            ResponseEntity<JsonNode> entity = restTemplate.getForEntity(requestURL, JsonNode.class);
            JsonNode powerListNode = entity.getBody().get("power").get("values");
            ObjectReader objectReader = objectMapper.readerFor(new TypeReference<List<SolaredgePowerDto>>(){});
            List<SolaredgePowerDto> powerDtoList = objectReader.readValue(powerListNode);
            List<Power> measurements = powerDtoList.stream().map(SolaredgePowerDto::toPower).collect(Collectors.toList());
            measurements.forEach((x) -> x.setSiteId(syncApi.getSiteId()));
            return measurements;
        }
        catch (Exception e) {
            throw new SolaredgeApiException(e.getMessage());
        }
    }

    private String getPowerURL(SolaredgeSyncApi syncApi, LocalDateTime from, LocalDateTime to) {
        return solarEdgeEndpoint + "/site/" + syncApi.getExternalSiteId() + "/power.json?"
                + "api_key=" + syncApi.getApiKey() + "&siteId=" + syncApi.getExternalSiteId()
                + "&startTime=" + from.format(formatter) + "&endTime=" + to.format(formatter);
    }

    private SolaredgeDataPeriodDto getDataPeriod(SolaredgeSyncApi syncApi) throws JsonProcessingException {
        String requestURL = solarEdgeEndpoint + "/site/" + syncApi.getExternalSiteId()
                + "/dataPeriod.json?api_key=" + syncApi.getApiKey();
        return objectMapper.readValue(requestURL, SolaredgeDataPeriodDto.class);
    }
}
