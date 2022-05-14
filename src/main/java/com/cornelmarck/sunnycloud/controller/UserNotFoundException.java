package com.cornelmarck.sunnycloud.controller;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String emailAddress) {
        super("Could not find user " + emailAddress);
    }
}
