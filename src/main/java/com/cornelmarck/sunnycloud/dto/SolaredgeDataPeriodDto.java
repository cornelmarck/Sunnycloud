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

    public LocalDate getStart() {
        return LocalDate.parse(startDate);
    }

    public LocalDate getEnd() {
        return LocalDate.parse(endDate);
    }

}
