package com.cornelmarck.sunnycloud.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.Getter;
import lombok.Setter;

@DynamoDBTable(tableName="Main")
public class SolaredgeApiConfig extends AbstractApiConfig {
    @Getter @Setter
    @DynamoDBAttribute(attributeName = "ExternalSiteId")
    private String externalSiteId;
    @Getter @Setter
    @DynamoDBAttribute(attributeName = "ApiKey")
    private String apiKey;

    public SolaredgeApiConfig() {}

    @Override
    public String getName() {
        return SyncApiType.SOLAREDGE.name();
    }
}
