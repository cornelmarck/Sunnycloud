package com.cornelmarck.sunnycloud;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class SunnycloudApplication {
    private static final Logger log = LoggerFactory.getLogger(SunnycloudApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(SunnycloudApplication.class, args);
    }

    @Bean
    public CommandLineRunner run(RestTemplate restTemplate) {
        return (args) -> {
            System.out.println("Hello world!");
        };
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

}
