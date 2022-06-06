package com.cornelmarck.sunnycloud.service;

import com.cornelmarck.sunnycloud.repository.ApiConfigRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ApiSchedulingService {
    private final ApiConfigRepository apiConfigRepository;
    private final SolaredgeApiService solaredgeApiService;

    @PostConstruct
    public void onStartup() {
        updateSolarEdge();
    }

    @Scheduled(cron = "0 1/31 * * * *")
    public void updateSolarEdge() {
        List<SolaredgeApiConfig> todo = apiConfigRepository.findAll();
        todo.forEach(solaredgeApiService::update);
    }

}
