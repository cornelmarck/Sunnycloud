package com.cornelmarck.sunnycloud.service;

import com.cornelmarck.sunnycloud.model.AbstractApiConfig;
import com.cornelmarck.sunnycloud.model.ApiConfigWrapper;
import com.cornelmarck.sunnycloud.model.SolaredgeApiConfig;
import com.cornelmarck.sunnycloud.model.SyncApiType;
import com.cornelmarck.sunnycloud.repository.ApiConfigRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class SiteSyncService {
    private final ApiConfigRepository apiConfigRepository;
    private final SolaredgeApiService solaredgeApiService;

    @Scheduled(cron = "0 1/31 * * * *")
    public void updateSolarEdge() {
        apiConfigRepository.findAllByType(SyncApiType.SOLAREDGE)
                .forEach(x -> solaredgeApiService.update(x.getSiteId(), (SolaredgeApiConfig) x.getApiConfig()));
    }

    public void updateSite(String siteId) {
        Optional<ApiConfigWrapper> wrapper = apiConfigRepository.findBySiteId(siteId);
        if (wrapper.isEmpty()) {
            return;
        }
        AbstractApiConfig config = wrapper.get().getApiConfig();

        if (config.getClass().getSimpleName().equals(SyncApiType.SOLAREDGE.name())) {
            solaredgeApiService.update(siteId, (SolaredgeApiConfig) config);
        }
    }
}
