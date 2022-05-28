package com.cornelmarck.sunnycloud;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.cornelmarck.sunnycloud.service.SolaredgeSiteService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(locations = "classpath:test.properties")
public class SolaredgeSiteServiceIntegrationTest {
    @Autowired
    private DynamoDBMapper dynamoDBMapper;
    @Autowired
    private AmazonDynamoDB amazonDynamoDB;
    @Autowired
    private SolaredgeSiteService solaredgeSiteService;
    @Value("${solaredge.test.apikey}")
    private String apikey;

    @Test
    public void one() {
        solaredgeSiteService.getAllSites(apikey);
    }
}
