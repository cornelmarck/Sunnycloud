package com.cornelmarck.sunnycloud.service;

import com.cornelmarck.sunnycloud.model.Site;
import com.cornelmarck.sunnycloud.repository.SiteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.util.Optional;
import java.util.TimeZone;

@Service
@RequiredArgsConstructor
public class SiteService {
    private final SiteRepository siteRepository;

    public Optional<ZoneId> getTimeZoneId(String siteId) {
        Optional<Site> site = siteRepository.findById(siteId);
        if (site.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(TimeZone.getTimeZone(site.get().getTimeZone()).toZoneId());
    }
}
