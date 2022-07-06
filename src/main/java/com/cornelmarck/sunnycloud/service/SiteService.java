package com.cornelmarck.sunnycloud.service;

import com.cornelmarck.sunnycloud.dto.DataPeriodDto;
import com.cornelmarck.sunnycloud.exception.SiteNotFoundException;
import com.cornelmarck.sunnycloud.model.Power;
import com.cornelmarck.sunnycloud.model.Site;
import com.cornelmarck.sunnycloud.repository.PowerRepository;
import com.cornelmarck.sunnycloud.repository.SiteRepository;
import com.cornelmarck.sunnycloud.util.DynamoDBDateTimeConverter;
import com.cornelmarck.sunnycloud.util.TimeUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.TimeZone;

@Service
@RequiredArgsConstructor
public class SiteService {
    private final SiteRepository siteRepository;
    private final PowerRepository powerRepository;

    public void deleteSite(String siteId) {
        deletePowerMeasurements(siteId, DynamoDBDateTimeConverter.MIN, DynamoDBDateTimeConverter.MAX);
        siteRepository.delete(siteId);
    }

    public void deletePowerMeasurements(String siteId, LocalDateTime from, LocalDateTime to) {
        getPowerBetween(siteId, from, to).forEach(powerRepository::delete);
    }

    public List<Power> getPowerBetween(String siteId, LocalDateTime from, LocalDateTime to) {
        checkIfSiteExists(siteId);
        return powerRepository.findAllBySiteIdBetween(siteId, from, to);
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
            LocalDateTime zero = DynamoDBDateTimeConverter.MIN;
            return new DataPeriodDto(siteId, zero, zero);
        }
        LocalDateTime begin = powerRepository.findEarliestBySiteId(siteId).orElseThrow().getTimestamp();
        LocalDateTime end = powerRepository.findLatestBySiteId(siteId).orElseThrow().getTimestamp().plusSeconds(1);
        return new DataPeriodDto(siteId, begin.atZone(zone).toLocalDateTime(), end.atZone(zone).toLocalDateTime());
    }

    public boolean powerIsEmpty(String siteId) {
        checkIfSiteExists(siteId);
        return powerRepository.findEarliestBySiteId(siteId).isEmpty();
    }
}
