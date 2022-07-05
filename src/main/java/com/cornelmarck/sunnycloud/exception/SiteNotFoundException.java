package com.cornelmarck.sunnycloud.exception;

public class SiteNotFoundException extends RuntimeException {
    public SiteNotFoundException(String message) {
        super(message);
    }
}
