package com.cornelmarck.sunnycloud.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.UUID;
import java.util.function.Supplier;

public class SitePrimaryKey implements Serializable {
    private String userId;
    private UUID siteId;

    public SitePrimaryKey() {}
    public SitePrimaryKey(String userId, UUID siteId) {
        setUserId(userId);
        setSiteId(siteId);
    }

    @DynamoDBHashKey
    @DynamoDBAttribute(attributeName = "UserEmailAddress")
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }

    public UUID getSiteId() {
        return siteId;
    }
    public void setSiteId(UUID siteId) {
        this.siteId = siteId;
    }

    @DynamoDBRangeKey
    @DynamoDBAttribute(attributeName = "SiteId_Type_Timestamp")
    public String getSortKey() {
        return siteId + "#" + "Site";
    }
    public void setSortKey(String sortKey) {
        siteId = UUID.fromString(sortKey.split("#")[0]);
    }

}
