package com.cornelmarck.sunnycloud.controller;

import com.cornelmarck.sunnycloud.config.DynamoDBInstantConverter;
import com.cornelmarck.sunnycloud.dto.PowerDto;
import com.cornelmarck.sunnycloud.model.Site;
import com.cornelmarck.sunnycloud.repository.PowerRepository;
import com.cornelmarck.sunnycloud.repository.SiteRepository;
import com.cornelmarck.sunnycloud.service.PowerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
public class SiteController {
    private final SiteRepository siteRepository;
    private final PowerRepository powerRepository;
    private final DynamoDBInstantConverter dynamoDBInstantConverter;
    private final PowerService powerService;

    //Get all sites filtering on owner
    @GetMapping("/sites")
    public List<Site> all(@RequestParam Optional<String> ownerId) {
        if (ownerId.isEmpty()) {
            return siteRepository.findAll();
        }
        return siteRepository.findByOwner(ownerId.get());
    }

    //Get one site
    @GetMapping("/sites/{siteId}")
    public Site one(@PathVariable String siteId) {
        return siteRepository.findById(siteId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Site not found: " + siteId));
    }

    @PostMapping("/sites")
    public Site create(@RequestBody Site newSite) {
        siteRepository.insert(newSite);
        return newSite;
    }

    @GetMapping("/sites/{siteId}/power")
    List<PowerDto> allBySiteId(@PathVariable String siteId, @RequestParam String from, @RequestParam String to) {
        if (siteRepository.findById(siteId).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Site not found: " + siteId);
        }
        try {
            return powerService.getBetweenLocalDateTime(siteId, LocalDateTime.parse(from), LocalDateTime.parse(to));
        }
        catch (DateTimeParseException dateTimeParseException) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid request");
        }
    }
}
