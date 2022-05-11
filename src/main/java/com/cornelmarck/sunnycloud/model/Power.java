package com.cornelmarck.sunnycloud.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
import java.util.UUID;

@DynamoDBTable(tableName="Main")
public class Power {
    @Id
    private PowerPrimaryKey primaryKey;

    private double power;

    public Power() {
        primaryKey = new PowerPrimaryKey();
    }
    public Power(String userId, UUID siteId, LocalDateTime dateTime) {
        primaryKey = new PowerPrimaryKey();
        primaryKey.setUserId(userId);
        primaryKey.setSiteId(siteId);
        primaryKey.setDateTime(dateTime);
    }
    public Power(String userId, UUID siteId, LocalDateTime dateTime, double power) {
        this(userId, siteId, dateTime);
        setPower(power);
    }

    @DynamoDBHashKey(attributeName = "EmailAddress")
    public String getUserId() {
        return primaryKey.getUserId();
    }
    public void setUserId(String userId) {
        primaryKey.setUserId(userId);
    }

    @DynamoDBRangeKey(attributeName = "Type_SiteId_Timestamp")
    public String getSortKey() {
        return primaryKey.getSortKey();
    }
    public void setSortKey(String sortKey) {
        primaryKey.setSortKey(sortKey);
    }

    @DynamoDBAttribute(attributeName = "power")
    public double getPower() {
        return power;
    }
    public void setPower(double power) {
        this.power = power;
    }

}
