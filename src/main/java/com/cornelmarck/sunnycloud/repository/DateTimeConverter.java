package com.cornelmarck.sunnycloud.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeConverter implements DynamoDBTypeConverter<String, LocalDateTime> {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");
    private static final LocalDateTime minTimestamp = LocalDateTime.parse("1970-01-01T00:00:00.000");
    private static final LocalDateTime maxTimestamp = LocalDateTime.parse("9999-12-31T23:59:59.999");

    public DateTimeConverter() {}

    @Override
    public String convert(LocalDateTime dateTime) {
        return dateTime.format(formatter);
    }

    @Override
    public LocalDateTime unconvert(String s) {
        return LocalDateTime.parse(s, formatter);
    }

    public LocalDateTime getMinTimestamp() {
        return minTimestamp;
    }

    public LocalDateTime getMaxTimestamp() {
        return maxTimestamp;
    }

    public String getMinTimestampString() {
        return convert(minTimestamp);
    }

    public String getMaxTimestampString() {
        return convert(maxTimestamp);
    }


}
