package com.cornelmarck.sunnycloud.service;

import com.cornelmarck.sunnycloud.dto.DataPeriodDto;
import com.cornelmarck.sunnycloud.dto.PowerDto;
import com.cornelmarck.sunnycloud.exception.SiteNotFoundException;
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
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SiteService {
    private final SiteRepository siteRepository;
    private final PowerRepository powerRepository;

    public List<PowerDto> getPowerDtoBetween(String siteId, LocalDateTime from, LocalDateTime to) {
        return getPowerBetween(siteId, from, to).stream()
                .map(x -> PowerDto.fromPower(x, getTimeZoneId(siteId)))
                .collect(Collectors.toList());
    }

    public void deletePowerMeasurements(String siteId, LocalDateTime from, LocalDateTime to) {
        getPowerBetween(siteId, from, to).forEach(powerRepository::delete);
    }

    public List<Power> getPowerBetween(String siteId, LocalDateTime from, LocalDateTime to) {
        checkIfSiteExists(siteId);
        ZoneId zoneId = getTimeZoneId(siteId);
        return powerRepository.findAllBySiteIdAndTimestampBetween(
                siteId, TimeUtils.toInstant(from, zoneId), TimeUtils.toInstant(to, zoneId));
    }

    public ZoneId getTimeZoneId(String siteId) {
        Site site = siteRepository.findById(siteId).orElseThrow(() -> new SiteNotFoundException(siteId));
        return TimeZone.getTimeZone(site.getTimeZone()).toZoneId();
    }

    public void checkIfSiteExists(String siteId) {
        siteRepository.findById(siteId).orElseThrow(() -> new SiteNotFoundException(siteId));
    }

    public DataPeriodDto getDataPeriod(String siteId) {
        checkIfSiteExists(siteId);
        ZoneId zone = getTimeZoneId(siteId);
        if (powerIsEmpty(siteId)) {
            LocalDateTime zero = DynamoDBInstantConverter.MIN.atZone(zone).toLocalDateTime();
            return new DataPeriodDto(siteId, zero, zero);
        }
        Instant begin = powerRepository.findEarliestBySiteId(siteId).orElseThrow().getTimestamp();
        Instant end = powerRepository.findLatestBySiteId(siteId).orElseThrow().getTimestamp().plusSeconds(1);
        return new DataPeriodDto(siteId, begin.atZone(zone).toLocalDateTime(), end.atZone(zone).toLocalDateTime());
    }

    public boolean powerIsEmpty(String siteId) {
        checkIfSiteExists(siteId);
        return powerRepository.findEarliestBySiteId(siteId).isEmpty();
    }
}
