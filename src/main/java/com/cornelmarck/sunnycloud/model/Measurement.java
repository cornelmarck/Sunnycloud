package com.cornelmarck.sunnycloud.model;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@DynamoDBTable(tableName="Main")
public class Measurement {
    @Getter @Setter @DynamoDBHashKey(attributeName = "Id") private String siteId;
    @Getter @Setter
        @DynamoDBTypeConverted(converter = DateTimeConverter.class)
        @DynamoDBRangeKey(attributeName = "SortKey")
        private LocalDateTime timestamp;
    @Getter @Setter @DynamoDBAttribute(attributeName = "PowerOutput") private double powerOutput;


    public Measurement() {}
    public Measurement(String siteId, LocalDateTime timestamp, double powerOutput) {
        setSiteId(siteId);
        setTimestamp(timestamp);
        setPowerOutput(powerOutput);
    }
}
