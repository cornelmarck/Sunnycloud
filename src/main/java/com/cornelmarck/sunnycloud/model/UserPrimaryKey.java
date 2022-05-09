package com.cornelmarck.sunnycloud.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;

import java.io.Serializable;
import java.util.Objects;

public class UserPrimaryKey implements Serializable {
    private String id;

    public UserPrimaryKey() {}
    public UserPrimaryKey(String userId) {
        setId(userId);
    }

    @DynamoDBHashKey(attributeName = "UserEmailAddress")
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    @DynamoDBRangeKey(attributeName = "Type_SiteId_Timestamp")
    public String getSortKey() {
        return "UserDetails";
    }
    public void setSortKey(String sortKey) {}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserPrimaryKey that = (UserPrimaryKey) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
