package com.cornelmarck.sunnycloud.dto;

import com.cornelmarck.sunnycloud.model.Location;
import com.cornelmarck.sunnycloud.model.Site;
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
    private LocationDto location;
    @Getter @Setter
    private PrimaryModuleDto primaryModule;

    public static class LocationDto {
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

    public static class PrimaryModuleDto {
        @Getter @Setter
        private String manufacturerName;
        @Getter @Setter
        private String modelName;
        @Getter @Setter
        private double maximumPower;
        @Getter @Setter
        double temperatureCoef;
    }

    public Site toSite() {
        Site site = new Site();
        site.setId(Long.toString(id));
        site.setName(name);
        site.setPeakPower(peakPower * 1E3);

        Location other = new Location();
        other.setCountry(location.country);
        other.setCity(location.city);
        other.setAddress1(location.address);
        other.setAddress2(location.address2);
        other.setZipCode(location.zip);
        other.setCountryCode(location.countryCode);
        other.setTimeZone(location.timeZone);
        site.setLocation(other);

        return site;
    }
}

