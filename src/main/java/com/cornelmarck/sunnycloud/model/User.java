package com.cornelmarck.sunnycloud.model;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import lombok.Getter;
import lombok.Setter;

@DynamoDBTable(tableName="Main")
public class User {
    @Getter @Setter @DynamoDBHashKey(attributeName="Id") private String emailAddress;
    @Getter @Setter @DynamoDBRangeKey(attributeName="SortKey") private String sortKey;
    @Getter @Setter @DynamoDBAttribute(attributeName = "Name") private String name;
    @Getter @Setter @DynamoDBAttribute(attributeName = "MobilePhoneNumber") private String mobilePhoneNumber;

    public User() {
        setSortKey("User");
    }

}
