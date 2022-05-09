package com.cornelmarck.sunnycloud.model;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import org.springframework.data.annotation.Id;

import java.util.UUID;

@DynamoDBTable(tableName="Main")
public class Site {
    @Id
    private SitePrimaryKey primaryKey;

    private String name;
    private String country;
    private String city;
    private String address;
    private String zip;
    private int timeZone;
    private String countryCode;

    public Site(String userId) {
        primaryKey = new SitePrimaryKey();
        primaryKey.setUserId(userId);
        primaryKey.setSiteId(UUID.randomUUID());
    }
    public Site() {
        this(null);
    }

    @DynamoDBHashKey
    @DynamoDBAttribute(attributeName = "UserEmailAddress")
    public String getUserId() {
        return primaryKey.getUserId();
    }
    public void setUserId(String id) {
        primaryKey.setUserId(id);
    }

    @DynamoDBRangeKey
    @DynamoDBAttribute(attributeName = "SiteId_Type_Timestamp")
    public String getSortKey() {
        return primaryKey.getSortKey();
    }
    public void setSortKey(String sortKey) {
        primaryKey.setSortKey(sortKey);
    }

    @DynamoDBAttribute(attributeName = "SiteName")
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    @DynamoDBAttribute(attributeName = "Country")
    public String getCountry() {
        return country;
    }
    public void setCountry(String country) {
        this.country = country;
    }

    @DynamoDBAttribute(attributeName = "City")
    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }

    @DynamoDBAttribute(attributeName = "Address")
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }

    @DynamoDBAttribute(attributeName = "Zip")
    public String getZip() {
        return zip;
    }
    public void setZip(String zip) {
        this.zip = zip;
    }

    @DynamoDBAttribute(attributeName = "TimeZone")
    public int getTimeZone() {
        return timeZone;
    }
    public void setTimeZone(int timeZone) {
        this.timeZone = timeZone;
    }

    @DynamoDBAttribute(attributeName = "CountryCode")
    public String getCountryCode() {
        return countryCode;
    }
    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }


}
