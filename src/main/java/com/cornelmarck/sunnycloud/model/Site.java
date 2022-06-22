package com.cornelmarck.sunnycloud.model;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@DynamoDBTable(tableName="Main")
@JsonIgnoreProperties(value={ "id" }, allowGetters=true)
public class Site {
    @Getter @Setter
    @DynamoDBHashKey(attributeName = "Id")
    private String id;

    @Getter @Setter
    @JsonIgnore
    @DynamoDBRangeKey(attributeName = "SortKey")
    private String sortKey;

    @Getter @Setter
    @DynamoDBAttribute(attributeName = "Name")
    private String name;

    @Getter @Setter
    @DynamoDBIndexHashKey(globalSecondaryIndexName = "SiteOwnerIndex", attributeName = "OwnerId")
    private String ownerId;

    @Getter @Setter
    @DynamoDBAttribute(attributeName = "TimeZone")
    private String timeZone;

    @Getter @Setter
    @DynamoDBAttribute(attributeName = "Location")
    private Location location;

    @Getter @Setter
    @DynamoDBAttribute(attributeName = "PeakPower")
    private double peakPower;

    @Getter @Setter
    @DynamoDBAttribute(attributeName = "AzimuthAngle")
    private double azimuthAngle;

    @Getter @Setter
    @DynamoDBAttribute(attributeName = "TiltAngle")
    private double tiltAngle;

    public Site() {
        setId(UUID.randomUUID().toString());
        setSortKey("Site");
    }
}
