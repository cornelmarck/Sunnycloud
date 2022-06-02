package com.cornelmarck.sunnycloud.dto;

import com.cornelmarck.sunnycloud.model.Measurement;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SolaredgePowerDto {
    @Getter @Setter
    private String date;
    @Getter @Setter
    private double value;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public Measurement toMeasurement() {
        Measurement measurement = new Measurement();
        measurement.setTimestamp(LocalDateTime.parse(date, formatter));
        measurement.setPowerOutput(value);
        return measurement;
    }

}
