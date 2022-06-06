package com.cornelmarck.sunnycloud.service;

import com.cornelmarck.sunnycloud.dto.PowerDto;
import com.cornelmarck.sunnycloud.model.Power;
import com.cornelmarck.sunnycloud.repository.PowerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PowerService {
    private final SiteService siteService;
    private final PowerRepository powerRepository;

    public List<PowerDto> getBetweenLocalDateTime(String siteId, LocalDateTime from, LocalDateTime to) {
        ZoneId zoneId = siteService.getTimeZoneId(siteId).orElseThrow();
        List<Power> powerList = powerRepository.findAllBySiteIdAndTimestampBetween(siteId, getInstant(from, zoneId), getInstant(to, zoneId));
        return powerList.stream().map(x -> PowerDto.fromPower(x, zoneId)).collect(Collectors.toList());
    }

    private Instant getInstant(LocalDateTime timestamp, ZoneId zoneId) {
        ZoneOffset offset = zoneId.getRules().getOffset(timestamp);
        return timestamp.toInstant(offset);
    }
}
