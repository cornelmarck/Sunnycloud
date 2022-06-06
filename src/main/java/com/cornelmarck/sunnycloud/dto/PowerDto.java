package com.cornelmarck.sunnycloud.dto;

import com.cornelmarck.sunnycloud.model.Power;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class PowerDto {
    @Getter @Setter
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private LocalDateTime timestamp;
    @Getter @Setter
    private double powerOutput;

    public static PowerDto fromPower(Power power, ZoneId zoneId) {
        PowerDto dto = new PowerDto();
        dto.setTimestamp(power.getTimestamp().atZone(zoneId).toLocalDateTime());
        dto.setPowerOutput(power.getPowerOutput());
        return dto;
    }
}
