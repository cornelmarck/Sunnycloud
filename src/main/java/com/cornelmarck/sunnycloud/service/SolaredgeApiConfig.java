package com.cornelmarck.sunnycloud.service;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.Getter;
import lombok.Setter;

@DynamoDBTable(tableName="Main")
public class SolaredgeApiConfig {
    @Getter @Setter @DynamoDBHashKey(attributeName = "Id")
    private String partitionKey;
    @Getter @Setter @DynamoDBRangeKey(attributeName = "SortKey")
    private String siteId;
    @Getter @Setter @DynamoDBAttribute(attributeName = "IsActive")
    private boolean isActive;
    @Getter @Setter @DynamoDBAttribute(attributeName = "ExternalSiteId")
    private String externalSiteId;
    @Getter @Setter @DynamoDBAttribute(attributeName = "ApiKey")
    private String apiKey;

    public SolaredgeApiConfig() {
        setPartitionKey(SyncApiType.SOLAREDGE.name());
    }

}
