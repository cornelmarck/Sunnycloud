package com.cornelmarck.sunnycloud.service;

import com.cornelmarck.sunnycloud.model.SolaredgeApiConfig;
import com.cornelmarck.sunnycloud.repository.SyncApiRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class SyncApiSchedulingService {
    private final SyncApiRepository syncApiRepository;
    private final SolaredgeSyncApiService solaredgeSyncApiService;

    @Scheduled(cron = "0 1/31 * * * *")
    public void updateSolarEdge() {
        List<SolaredgeApiConfig> todo = syncApiRepository.findAll();
        todo.forEach(solaredgeSyncApiService::update);
    }

}
