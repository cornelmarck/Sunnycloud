package com.cornelmarck.sunnycloud.service;

import com.cornelmarck.sunnycloud.model.AbstractApiConfig;
import com.cornelmarck.sunnycloud.model.Site;
import com.cornelmarck.sunnycloud.model.SolaredgeApiConfig;
import com.cornelmarck.sunnycloud.model.SyncApiType;
import com.cornelmarck.sunnycloud.repository.SiteRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class SiteSyncService {
    private final Logger logger = LoggerFactory.getLogger(SiteSyncService.class);
    private final SiteRepository siteRepository;
    private final SolaredgeApiService solaredgeApiService;

    @Scheduled(cron = "0 1/31 * * * *")
    public void updateSolarEdge() {
        logger.info("Scheduled Solaredge synchronisation");
        for (Site site : siteRepository.findAllBySyncApiType(SyncApiType.SOLAREDGE)) {
            solaredgeApiService.updateSite(site.getId(), (SolaredgeApiConfig) site.getApiConfig());
        }
    }

    public void updateSite(String siteId) {
        logger.debug("Instantiated site synchronisation: " + siteId);
        Optional<Site> site = siteRepository.findById(siteId);
        if (site.isEmpty() || site.get().getApiConfig() == null) {
            return;
        }
        AbstractApiConfig config = site.get().getApiConfig();
        if (!config.isActive()) {
            return;
        }
        if (config instanceof SolaredgeApiConfig) {
            solaredgeApiService.updateSite(siteId, (SolaredgeApiConfig) config);
        }
    }
}
