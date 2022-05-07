package com.cornelmarck.sunnycloud.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Site {
    public final long id;
    public final String name;
    public final long accountId;
    public final double peakPower;
    public final String installationDate;
    public final Location location;
    public final PrimaryModule primaryModule;

    public Site(long id, String name, long accountId, double peakPower, String installationDate, Location location, PrimaryModule primaryModule) {
        this.id = id;
        this.name = name;
        this.accountId = accountId;
        this.peakPower = peakPower;
        this.installationDate = installationDate;
        this.location = location;
        this.primaryModule = primaryModule;
    }

    public static class Location {
        public final String country;
        public final String city;
        public final String address1;
        public final String address2;
        public final String zip;
        public final String timeZone;
        public final String countryCode;

        public Location(String country, String city, String address1, String address2, String zip, String timeZone, String countryCode) {
            this.country = country;
            this.city = city;
            this.address1 = address1;
            this.address2 = address2;
            this.zip = zip;
            this.timeZone = timeZone;
            this.countryCode = countryCode;
        }
    }

    public static class PrimaryModule {
        public final String manufacturerName;
        public final String modelName;
        public final double maximumPower;
        public final double temperatureCoef;

        public PrimaryModule(String manufacturerName, String modelName, double maximumPower, double temperatureCoef) {
            this.manufacturerName = manufacturerName;
            this.modelName = modelName;
            this.maximumPower = maximumPower;
            this.temperatureCoef = temperatureCoef;
        }
    }
}

