package com.cornelmarck.sunnycloud.model;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.cornelmarck.sunnycloud.config.DynamoDBDateTimeConverter;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@DynamoDBTable(tableName="Main")
public class Measurement {
    @Getter @Setter @DynamoDBHashKey(attributeName = "Id")
    private String siteId;
    @Getter @Setter
    @DynamoDBTypeConverted(converter = DynamoDBDateTimeConverter.class) @DynamoDBRangeKey(attributeName = "SortKey")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private LocalDateTime timestamp;
    @Getter @Setter @DynamoDBAttribute(attributeName = "PowerOutput")
    private double powerOutput;

    public Measurement() {}
    public Measurement(String siteId, LocalDateTime timestamp, double powerOutput) {
        setSiteId(siteId);
        setTimestamp(timestamp);
        setPowerOutput(powerOutput);
    }
}
