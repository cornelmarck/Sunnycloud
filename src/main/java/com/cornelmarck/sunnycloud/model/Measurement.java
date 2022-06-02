package com.cornelmarck.sunnycloud.model;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.cornelmarck.sunnycloud.config.DynamoDBDateTimeConverter;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@DynamoDBTable(tableName="Main")
public class Measurement {
    @Getter @Setter
    @DynamoDBHashKey(attributeName = "Id")
    @JsonIgnore
    private String siteId;

    @Getter @Setter
    @DynamoDBRangeKey(attributeName = "SortKey")
    @DynamoDBTypeConverted(converter = DynamoDBDateTimeConverter.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private LocalDateTime timestamp;

    @Getter @Setter @DynamoDBAttribute(attributeName = "PowerOutput")
    private double powerOutput;

    public Measurement() {}

}
