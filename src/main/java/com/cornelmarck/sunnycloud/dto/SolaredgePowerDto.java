package com.cornelmarck.sunnycloud.dto;

import com.cornelmarck.sunnycloud.model.Power;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class SolaredgePowerDto {
    @Getter @Setter
    private String date;
    @Getter @Setter
    private double value;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public Power toPower(String siteId) {
        Power power = new Power();
        power.setSiteId(siteId);
        power.setTimestamp(LocalDateTime.parse(date, formatter));
        power.setPowerOutput(value);
        return power;
    }
}
