package com.cornelmarck.sunnycloud.model;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import org.springframework.data.annotation.Id;

import java.util.UUID;

@DynamoDBTable(tableName="Main")
public class User {
    @Id
    private UserPrimaryKey primaryKey;

    private String mobileNumber;

    public User(String emailAddress) {
        this.primaryKey = new UserPrimaryKey(emailAddress);
    }
    public User() {
        this(null);
    }

    @DynamoDBHashKey(attributeName = "UserEmailAddress")
    public String getId() {
        return primaryKey.getId();
    }
    public void setId(String id) {
        primaryKey.setId(id);
    }

    @DynamoDBRangeKey(attributeName = "SiteId_Mode_Timestamp")
    public String getSortKey() {
        return primaryKey.getSortKey();
    }
    public void setSortKey(String sortKey) {
        primaryKey.setSortKey(sortKey);
    }

    @DynamoDBAttribute(attributeName = "MobileNumber")
    public String getMobileNumber() {
        return mobileNumber;
    }
    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

}
