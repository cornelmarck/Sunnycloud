package com.cornelmarck.sunnycloud.model;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.cornelmarck.sunnycloud.util.DynamoDBDateTimeConverter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@DynamoDBTable(tableName="Power")
public class Power {
    @Getter @Setter
    @DynamoDBHashKey(attributeName = "Id")
    private String siteId;

    @Getter @Setter
    @DynamoDBRangeKey(attributeName = "SortKey")
    @DynamoDBTypeConverted(converter = DynamoDBDateTimeConverter.class)
    private LocalDateTime timestamp;

    @Getter @Setter
    @DynamoDBAttribute(attributeName = "PowerOutput")
    private double powerOutput;

    public Power() {}

}
