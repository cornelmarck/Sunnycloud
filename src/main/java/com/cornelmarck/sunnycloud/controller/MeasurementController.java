package com.cornelmarck.sunnycloud.controller;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import com.cornelmarck.sunnycloud.model.DateTimeConverter;
import com.cornelmarck.sunnycloud.model.Measurement;
import com.cornelmarck.sunnycloud.repository.MeasurementRepository;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
public class MeasurementController {
    private final MeasurementRepository repository;
    private final DateTimeConverter converter;
    private final LocalDateTime minTimestamp;
    private final LocalDateTime maxTimestamp;

    public MeasurementController(MeasurementRepository repository, DateTimeConverter dateTimeConverter,
                                 LocalDateTime minTimestamp, LocalDateTime maxTimestamp) {
        this.repository = repository;
        this.converter = dateTimeConverter;
        this.minTimestamp = minTimestamp;
        this.maxTimestamp = maxTimestamp;
    }

    @GetMapping("/measurements/{siteId}")
    List<Measurement> allBySiteId(@PathVariable String siteId, @RequestParam Optional<String> from, @RequestParam Optional<String> to) {
        LocalDateTime start = from.map(LocalDateTime::parse).orElse(minTimestamp);
        LocalDateTime end = to.map(LocalDateTime::parse).orElse(maxTimestamp);

        return repository.findAllBySiteIdAndTimestampBetween(siteId, start, end);
    }


}
