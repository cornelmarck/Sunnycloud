package com.cornelmarck.sunnycloud.util;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class DynamoDBInstantConverter implements DynamoDBTypeConverter<String, Instant> {
    public static final Instant MIN = Instant.EPOCH;
    public static final Instant MAX = Instant.parse("9999-12-31T23:59:59Z");
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
            .withZone(ZoneId.of("UTC"));

    @Override
    public String convert(Instant instant) {
        return formatter.format(instant);
    }

    @Override
    public Instant unconvert(String s) {
        return Instant.parse(s);
    }
}
