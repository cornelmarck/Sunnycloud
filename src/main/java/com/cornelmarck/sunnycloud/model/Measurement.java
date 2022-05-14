package com.cornelmarck.sunnycloud.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import lombok.Getter;
import lombok.Setter;

@DynamoDBTable(tableName="Main")
public class Measurement {
    @Getter @Setter @DynamoDBHashKey(attributeName = "Id") String siteId;
    @Getter @Setter @DynamoDBRangeKey(attributeName = "SortKey") String timestamp;
    @Getter @Setter @DynamoDBAttribute(attributeName = "PowerOutput") double powerOutput;

    public Measurement() {}
    public Measurement(String siteId, String timestamp, double powerOutput) {
        setSiteId(siteId);
        setTimestamp(timestamp);
        setPowerOutput(powerOutput);
    }
}
