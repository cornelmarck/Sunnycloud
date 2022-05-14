package com.cornelmarck.sunnycloud.controller;

import com.cornelmarck.sunnycloud.model.Site;
import com.cornelmarck.sunnycloud.repository.SiteRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class SiteController {
    private final SiteRepository repository;

    public SiteController(SiteRepository siteRepository) {
        this.repository = siteRepository;
    }





}
