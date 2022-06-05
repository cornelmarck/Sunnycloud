package com.cornelmarck.sunnycloud.service;

import com.cornelmarck.sunnycloud.repository.SyncApiRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SyncApiSchedulingService {
    private final SyncApiRepository syncApiRepository;
    private final SolaredgeSyncApiService solaredgeSyncApiService;

    @Scheduled()
    public void updateSolarEdge() {

    }

}
