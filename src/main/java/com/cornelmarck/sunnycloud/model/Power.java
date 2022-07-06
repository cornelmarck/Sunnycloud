package com.cornelmarck.sunnycloud.model;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.cornelmarck.sunnycloud.util.DynamoDBDateTimeConverter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
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
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime timestamp;

    @Getter @Setter
    @DynamoDBAttribute(attributeName = "PowerOutput")
    private double powerOutput;

    public Power() {}

}
