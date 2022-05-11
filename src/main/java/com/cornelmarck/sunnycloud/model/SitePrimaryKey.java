package com.cornelmarck.sunnycloud.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Objects;
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
    @DynamoDBAttribute(attributeName = "EmailAddress")
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
    @DynamoDBAttribute(attributeName = "Type_SiteId_Timestamp")
    public String getSortKey() {
        return "SiteDetails#" + siteId;
    }
    public void setSortKey(String sortKey) {
        siteId = UUID.fromString(sortKey.split("#")[1]);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SitePrimaryKey that = (SitePrimaryKey) o;
        return Objects.equals(userId, that.userId) && Objects.equals(siteId, that.siteId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, siteId);
    }
}
