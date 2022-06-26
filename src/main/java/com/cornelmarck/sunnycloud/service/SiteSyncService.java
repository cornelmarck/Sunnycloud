package com.cornelmarck.sunnycloud.service;

import com.cornelmarck.sunnycloud.model.AbstractApiConfig;
import com.cornelmarck.sunnycloud.model.ApiConfigWrapper;
import com.cornelmarck.sunnycloud.model.SolaredgeApiConfig;
import com.cornelmarck.sunnycloud.model.SyncApiType;
import com.cornelmarck.sunnycloud.repository.ApiConfigRepository;
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
    private final ApiConfigRepository apiConfigRepository;
    private final SolaredgeApiService solaredgeApiService;

    @Scheduled(cron = "* 1/31 * * ? *")
    public void updateSolarEdge() {
        logger.info("Scheduled Solaredge synchronisation");
        apiConfigRepository.findAllByType(SyncApiType.SOLAREDGE)
                .forEach(x -> solaredgeApiService.updateSite(x.getSiteId(), (SolaredgeApiConfig) x.getApiConfig()));
    }

    public void updateSite(String siteId) {
        logger.debug("Instantiated site synchronisation: " + siteId);
        Optional<ApiConfigWrapper> wrapper = apiConfigRepository.findBySiteId(siteId);
        if (wrapper.isEmpty()) {
            return;
        }
        AbstractApiConfig config = wrapper.get().getApiConfig();

        if (config instanceof SolaredgeApiConfig) {
            solaredgeApiService.updateSite(siteId, (SolaredgeApiConfig) config);
        }
    }
}
