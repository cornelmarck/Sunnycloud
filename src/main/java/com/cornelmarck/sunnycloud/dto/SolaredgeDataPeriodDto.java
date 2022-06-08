package com.cornelmarck.sunnycloud.dto;

import com.cornelmarck.sunnycloud.util.TimeUtils;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class SolaredgeDataPeriodDto {
    @Getter @Setter
    private String startDate;
    @Getter @Setter
    private String endDate;

    public Instant getStart(ZoneId zoneId) {
        return TimeUtils.toInstant(LocalDate.parse(startDate).atStartOfDay(zoneId).toLocalDateTime(), zoneId);
    }

    public Instant getEnd(ZoneId zoneId) {
        return TimeUtils.toInstant(LocalDate.parse(endDate).plusDays(1).atStartOfDay(zoneId).toLocalDateTime(), zoneId);
    }

}
