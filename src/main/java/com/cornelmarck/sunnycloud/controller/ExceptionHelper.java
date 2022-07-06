package com.cornelmarck.sunnycloud.controller;

import com.cornelmarck.sunnycloud.exception.RequestConflictException;
import com.cornelmarck.sunnycloud.exception.SiteNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.format.DateTimeParseException;

@ControllerAdvice
public class ExceptionHelper {
    @ExceptionHandler(value = SiteNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void handleInvalidSite() {}

    @ExceptionHandler(value = DateTimeParseException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void handleInvalidDateTime() {}

    @ExceptionHandler(value = RequestConflictException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void handleRequestConflict() {}

}
