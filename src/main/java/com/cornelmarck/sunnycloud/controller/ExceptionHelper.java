package com.cornelmarck.sunnycloud.controller;

import com.cornelmarck.sunnycloud.exception.RequestConflictException;
import com.cornelmarck.sunnycloud.exception.SiteNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.format.DateTimeParseException;

@ControllerAdvice
public class ExceptionHelper {
    @ExceptionHandler(value = SiteNotFoundException.class)
    public ResponseEntity<Object> handleInvalidSite(SiteNotFoundException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = DateTimeParseException.class)
    public ResponseEntity<Object> handleInvalidDateTime(DateTimeParseException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<Object> handleRequestConflict(RequestConflictException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

}
