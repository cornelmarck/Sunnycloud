package com.cornelmarck.sunnycloud.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SolaredgeConfig {
    @Bean
    public String solaredgeEndpoint() {
        return "https://monitoringapi.solaredge.com";
    }
}
