package com.cornelmarck.sunnycloud.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeConverter implements DynamoDBTypeConverter<String, LocalDateTime> {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    public DateTimeConverter() {}

    @Override
    public String convert(LocalDateTime dateTime) {
        return dateTime.format(formatter);
    }

    @Override
    public LocalDateTime unconvert(String s) {
        return LocalDateTime.parse(s, formatter);
    }
}
