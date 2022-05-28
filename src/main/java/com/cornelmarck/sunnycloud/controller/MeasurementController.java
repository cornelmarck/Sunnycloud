package com.cornelmarck.sunnycloud.controller;

import com.cornelmarck.sunnycloud.repository.DateTimeConverter;
import com.cornelmarck.sunnycloud.model.Measurement;
import com.cornelmarck.sunnycloud.repository.MeasurementRepository;
import com.cornelmarck.sunnycloud.repository.SiteRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

@RestController
public class MeasurementController {
    private final MeasurementRepository measurementRepository;
    private final SiteRepository siteRepository;
    private final DateTimeConverter dateTimeConverter;

    public MeasurementController(MeasurementRepository measurementRepository, SiteRepository siteRepository, DateTimeConverter dateTimeConverter) {
        this.measurementRepository = measurementRepository;
        this.siteRepository = siteRepository;
        this.dateTimeConverter = dateTimeConverter;
    }

    @GetMapping("/measurements/{siteId}")
    List<Measurement> allBySiteId(@PathVariable String siteId, @RequestParam Optional<String> from, @RequestParam Optional<String> to) {
        if (siteRepository.findById(siteId).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Site not found: " + siteId);
        }

        try {
            return measurementRepository.findAllBySiteIdAndTimestampBetween(siteId, from, to);
        }
        catch (DateTimeParseException dateTimeParseException) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid request");
        }


    }

}
