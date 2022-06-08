package com.cornelmarck.sunnycloud.service;

import com.cornelmarck.sunnycloud.repository.ApiConfigRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ApiSchedulingService {
    private final ApiConfigRepository apiConfigRepository;
    private final SolaredgeApiService solaredgeApiService;

    @Scheduled(cron = "0 1/31 * * * *")
    public void updateSolarEdge() {
        apiConfigRepository.findAll().forEach(solaredgeApiService::update);
    }
}
