package com.cornelmarck.sunnycloud.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import lombok.Getter;
import lombok.Setter;

@DynamoDBDocument
public class Location {
    @Getter @Setter @DynamoDBAttribute(attributeName = "Country")
    private String country;
    @Getter @Setter @DynamoDBAttribute(attributeName = "City")
    private String city;
    @Getter @Setter @DynamoDBAttribute(attributeName = "Address1")
    private String address1;
    @Getter @Setter @DynamoDBAttribute(attributeName = "Address2")
    private String address2;
    @Getter @Setter @DynamoDBAttribute(attributeName = "ZipCode")
    private String zipCode;
    @Getter @Setter @DynamoDBAttribute(attributeName = "Longitude")
    private double longitude;
    @Getter @Setter @DynamoDBAttribute(attributeName = "Latitude")
    private double latitude;
}
