package com.cornelmarck.sunnycloud.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SolaredgeSiteDto {
    @Getter @Setter
    private long id;
    @Getter @Setter
    private String name;
    @Getter @Setter
    private long accountId;
    @Getter @Setter
    private double peakPower;
    @Getter @Setter
    private String installationDate;
    @Getter @Setter
    private Location location;
    @Getter @Setter
    private PrimaryModule primaryModule;

    public static class Location {
        @Getter @Setter
        private String country;
        @Getter @Setter
        private String city;
        @Getter @Setter
        private String address;
        @Getter @Setter
        private String address2;
        @Getter @Setter
        private String zip;
        @Getter @Setter
        private String timeZone;
        @Getter @Setter
        private String countryCode;
    }

    public static class PrimaryModule {
        @Getter @Setter
        private String manufacturerName;
        @Getter @Setter
        private String modelName;
        @Getter @Setter
        private double maximumPower;
        @Getter @Setter
        double temperatureCoef;
    }
}

