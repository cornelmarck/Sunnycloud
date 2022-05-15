package com.cornelmarck.sunnycloud.controller;

import com.cornelmarck.sunnycloud.model.Site;
import com.cornelmarck.sunnycloud.repository.SiteRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SiteController {
    private final SiteRepository repository;

    public SiteController(SiteRepository siteRepository) {
        this.repository = siteRepository;
    }

    @GetMapping("/sites/{siteId}")
    Site one(@PathVariable String siteId) {
        return repository.findById(siteId)
                .orElseThrow(() -> new SiteNotFoundException(siteId));
    }

    @GetMapping("/sites")
    List<Site> all() {
        return repository.findAll();
    }

}
