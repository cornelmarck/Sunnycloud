package com.cornelmarck.sunnycloud.service;

import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedList;
import com.cornelmarck.sunnycloud.dto.DataPeriodDto;
import com.cornelmarck.sunnycloud.dto.PowerDto;
import com.cornelmarck.sunnycloud.model.Power;
import com.cornelmarck.sunnycloud.model.Site;
import com.cornelmarck.sunnycloud.repository.PowerRepository;
import com.cornelmarck.sunnycloud.repository.SiteRepository;
import com.cornelmarck.sunnycloud.util.DynamoDBInstantConverter;
import com.cornelmarck.sunnycloud.util.TimeUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SiteService {
    private final SiteRepository siteRepository;
    private final PowerRepository powerRepository;

    public ZoneId getTimeZoneId(String siteId) {
        Site site = siteRepository.findById(siteId).orElseThrow();
        return TimeZone.getTimeZone(site.getTimeZone()).toZoneId();
    }

    public List<PowerDto> getBetweenLocalDateTime(String siteId, LocalDateTime from, LocalDateTime to) {
        ZoneId zoneId = getTimeZoneId(siteId);
        List<Power> powerList = powerRepository.findAllBySiteIdAndTimestampBetween(
                siteId, TimeUtils.toInstant(from, zoneId), TimeUtils.toInstant(to, zoneId));
        return powerList.stream()
                .map(x -> PowerDto.fromPower(x, zoneId))
                .collect(Collectors.toList());
    }

    public boolean powerIsEmpty(String siteId) {
        siteRepository.findById(siteId).orElseThrow();
        return powerRepository.findEarliestBySiteId(siteId).isEmpty();
    }

    public int numberOfPowerMeasurements(String siteId) {
        siteRepository.findById(siteId).orElseThrow();
        return powerRepository.size(siteId);
    }

    public DataPeriodDto getDataPeriod(String siteId) {
        Site site = siteRepository.findById(siteId).orElseThrow();
        ZoneId zone = getTimeZoneId(siteId);
        if (powerIsEmpty(siteId)) {
            LocalDateTime zero = DynamoDBInstantConverter.MIN.atZone(zone).toLocalDateTime();
            return new DataPeriodDto(zero, zero);
        }
        Instant begin = powerRepository.findEarliestBySiteId(siteId).orElseThrow().getTimestamp();
        Instant end = powerRepository.findLatestBySiteId(siteId).orElseThrow().getTimestamp().plusSeconds(1);
        return new DataPeriodDto(begin.atZone(zone).toLocalDateTime(), end.atZone(zone).toLocalDateTime());
    }


}
