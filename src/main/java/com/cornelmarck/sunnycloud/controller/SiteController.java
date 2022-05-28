package com.cornelmarck.sunnycloud.controller;

import com.cornelmarck.sunnycloud.model.Site;
import com.cornelmarck.sunnycloud.repository.SiteRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
public class SiteController {
    private final SiteRepository siteRepository;

    public SiteController(SiteRepository siteRepository) {
        this.siteRepository = siteRepository;
    }

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
}
