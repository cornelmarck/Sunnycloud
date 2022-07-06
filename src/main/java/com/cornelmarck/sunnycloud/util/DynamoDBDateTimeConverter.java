package com.cornelmarck.sunnycloud.util;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import org.slf4j.spi.LocationAwareLogger;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class DynamoDBDateTimeConverter implements DynamoDBTypeConverter<String, LocalDateTime> {
    public static final LocalDateTime MIN = LocalDateTime.parse("1970-01-01T00:00:00");
    public static final LocalDateTime MAX = LocalDateTime.parse("9999-12-31T23:59:59");
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    @Override
    public String convert(LocalDateTime localDateTime) {
        return formatter.format(localDateTime);
    }

    @Override
    public LocalDateTime unconvert(String s) {
        return LocalDateTime.parse(s);
    }
}
