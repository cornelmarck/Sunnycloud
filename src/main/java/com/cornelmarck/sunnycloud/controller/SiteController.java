package com.cornelmarck.sunnycloud.controller;

import com.cornelmarck.sunnycloud.dto.DataPeriodDto;
import com.cornelmarck.sunnycloud.dto.PowerDto;
import com.cornelmarck.sunnycloud.exception.RequestConflictException;
import com.cornelmarck.sunnycloud.exception.SiteNotFoundException;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
public class SiteController {
    private final Logger logger = LoggerFactory.getLogger(SiteSyncService.class);

    private final SiteRepository siteRepository;
    private final PowerRepository powerRepository;
    private final DynamoDBInstantConverter dynamoDBInstantConverter;
    private final SiteService siteService;
    private final SiteSyncService siteSyncService;

    @GetMapping("/sites")
    @ResponseStatus(HttpStatus.OK)
    public List<Site> all(@RequestParam Optional<String> ownerId) {
        if (ownerId.isEmpty()) {
            return siteRepository.findAll();
        }
        return siteRepository.findByOwner(ownerId.get());
    }

    @GetMapping("/sites/{siteId}")
    @ResponseStatus(HttpStatus.OK)
    public Site one(@PathVariable String siteId) {
        return siteRepository.findById(siteId).orElseThrow(() -> new SiteNotFoundException(siteId));
    }

    @DeleteMapping("/sites/{siteId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String siteId) {
        siteService.deleteSite(siteId);
    }

    @PostMapping("/sites")
    @ResponseStatus(HttpStatus.CREATED)
    public Site create(@RequestBody Site newSite) {
        siteRepository.save(newSite);
        return newSite;
    }

    @PutMapping("/sites/{siteId}")
    public ResponseEntity<String> updateSite(@PathVariable String siteId, @RequestBody Site site) {
        if (!siteId.equals(site.getId())) {
            throw new RequestConflictException(String.format("Parameter: %s; body: %s", siteId, site.getId()));
        }
        boolean alreadyExists = siteRepository.findById(siteId).isPresent();
        siteRepository.save(site);
        siteSyncService.updateSite(siteId);

        if (alreadyExists) {
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @GetMapping("/sites/{siteId}/power")
    @ResponseStatus(HttpStatus.OK)
    public List<PowerDto> allBySiteId(@PathVariable String siteId, @RequestParam String from, @RequestParam String to) {
        return siteService.getPowerDtoBetween(siteId, LocalDateTime.parse(from), LocalDateTime.parse(to));
    }

    @DeleteMapping("/sites/{siteId}/power")
    @ResponseStatus(HttpStatus.OK)
    public void deleteBySiteId(@PathVariable String siteId, @RequestParam String from, @RequestParam String to) {
        if (siteRepository.findById(siteId).isEmpty()) {
            throw new SiteNotFoundException(siteId);
        }
        siteService.deletePowerMeasurements(siteId, LocalDateTime.parse(from), LocalDateTime.parse(to));
    }

    @GetMapping("/sites/{siteId}/dataPeriod")
    @ResponseStatus(HttpStatus.OK)
    public DataPeriodDto dataPeriod(@PathVariable String siteId) {
        if (siteRepository.findById(siteId).isEmpty()) {
            throw new SiteNotFoundException(siteId);
        }
        return siteService.getDataPeriod(siteId);
    }
}
