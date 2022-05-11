package com.cornelmarck.sunnycloud.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import org.springframework.data.annotation.Id;

import java.util.Objects;

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

    @DynamoDBHashKey(attributeName = "EmailAddress")
    public String getId() {
        return primaryKey.getId();
    }
    public void setId(String id) {
        primaryKey.setId(id);
    }

    @DynamoDBRangeKey(attributeName = "Type_SiteId_Timestamp")
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(primaryKey, user.primaryKey) && Objects.equals(mobileNumber, user.mobileNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(primaryKey, mobileNumber);
    }

    @Override
    public String toString() {
        return "User{" +
                "emailAddress=" + primaryKey.getId() + ", mobileNumber=" + getMobileNumber()
                + '}';
    }
}
