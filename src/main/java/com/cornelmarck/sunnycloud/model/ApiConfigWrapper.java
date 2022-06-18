package com.cornelmarck.sunnycloud.model;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import lombok.Getter;
import lombok.Setter;

@DynamoDBTable(tableName="Main")
public class ApiConfigWrapper {
    @Getter @Setter
    @DynamoDBHashKey(attributeName = "Id")
    private String siteId;

    @Getter @Setter
    @DynamoDBRangeKey(attributeName = "SortKey")
    private String sortKey;

    @DynamoDBAttribute(attributeName = "SyncApiType")
    public String getType() {
        return apiConfig.getName();
    }
    public void setType(String type) {}

    @Getter @Setter
    @DynamoDBAttribute(attributeName = "ApiConfig")
    private AbstractApiConfig apiConfig;

    public ApiConfigWrapper() {
        setSortKey("SyncApi");
    }
}
