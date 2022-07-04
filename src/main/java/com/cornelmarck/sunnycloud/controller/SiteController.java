package com.cornelmarck.sunnycloud.controller;

import com.cornelmarck.sunnycloud.dto.DataPeriodDto;
import com.cornelmarck.sunnycloud.dto.PowerDto;
import com.cornelmarck.sunnycloud.exception.SolaredgeApiException;
import com.cornelmarck.sunnycloud.model.AbstractApiConfig;
import com.cornelmarck.sunnycloud.model.Site;
import com.cornelmarck.sunnycloud.repository.PowerRepository;
import com.cornelmarck.sunnycloud.repository.SiteRepository;
import com.cornelmarck.sunnycloud.service.SiteService;
import com.cornelmarck.sunnycloud.service.SiteSyncService;
import com.cornelmarck.sunnycloud.util.DynamoDBInstantConverter;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
public class SiteController {
    private final Logger logger = LoggerFactory.getLogger(SiteSyncService.class);

    private final SiteRepository siteRepository;
    private final PowerRepository powerRepository;
    private final DynamoDBInstantConverter dynamoDBInstantConverter;
    private final SiteService siteService;
    private final SiteSyncService siteSyncService;

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
        siteRepository.save(newSite);
        return newSite;
    }

    @PutMapping("/sites/{siteId}")
    public void updateSite(@PathVariable String siteId, @RequestBody Site site) {
        if (!siteId.equals(site.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "siteId does not match");
        }
        siteRepository.save(site);
        logger.info("Updating site siteId={}", siteId);
        siteSyncService.updateSite(siteId);
    }

    @GetMapping("/sites/{siteId}/power")
    public List<PowerDto> allBySiteId(@PathVariable String siteId, @RequestParam String from, @RequestParam String to) {
        if (siteRepository.findById(siteId).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Site not found: " + siteId);
        }
        try {
            return siteService.getPowerDtoBetween(siteId, LocalDateTime.parse(from), LocalDateTime.parse(to));
        }
        catch (DateTimeParseException dateTimeParseException) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid request");
        }
    }

    @DeleteMapping("/sites/{siteId}/power")
    public void deleteBySiteId(@PathVariable String siteId, @RequestParam String from, @RequestParam String to) {
        if (siteRepository.findById(siteId).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Site not found: " + siteId);
        }
        try {
            siteService.deletePowerMeasurements(siteId, LocalDateTime.parse(from), LocalDateTime.parse(to));
        }
        catch (DateTimeParseException dateTimeParseException) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid request");
        }
    }

    @GetMapping("/sites/{siteId}/dataPeriod")
    public DataPeriodDto dataPeriod(@PathVariable String siteId) {
        if (siteRepository.findById(siteId).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Site not found: " + siteId);
        }
        return siteService.getDataPeriod(siteId);
    }
}
