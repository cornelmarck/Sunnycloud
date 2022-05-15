package com.cornelmarck.sunnycloud.controller;

public class SiteNotFoundException extends RuntimeException {
    public SiteNotFoundException(String siteId) {
        super("Could not find site " + siteId);
    }
}
