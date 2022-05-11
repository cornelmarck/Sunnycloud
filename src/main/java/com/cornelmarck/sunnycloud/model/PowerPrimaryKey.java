package com.cornelmarck.sunnycloud.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIgnore;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

public class PowerPrimaryKey implements Serializable {
    private String userId;
    private UUID siteId;
    private LocalDateTime dateTime;

    public PowerPrimaryKey() {}
    public PowerPrimaryKey(String userId, UUID siteId, LocalDateTime dateTime) {
        setUserId(userId);
        setSiteId(siteId);
        setDateTime(dateTime);
    }

    @DynamoDBIgnore
    public UUID getSiteId() {
        return siteId;
    }
    public void setSiteId(UUID siteId) {
        this.siteId = siteId;
    }

    @DynamoDBIgnore
    public LocalDateTime getDateTime() {
        return dateTime;
    }
    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    @DynamoDBHashKey(attributeName = "EmailAddress")
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }

    @DynamoDBRangeKey(attributeName = "Type_SiteId_Timestamp")
    public String getSortKey() {
        return "Power#" + siteId + "#" + dateTime.toString();
    }
    public void setSortKey(String sortKey) {
        String[] items = sortKey.split("#");
        setSiteId(UUID.fromString(items[1]));
        setDateTime(LocalDateTime.parse(items[2]));
    }

}
