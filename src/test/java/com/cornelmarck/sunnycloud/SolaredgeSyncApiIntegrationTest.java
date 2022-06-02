package com.cornelmarck.sunnycloud;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.cornelmarck.sunnycloud.model.Site;
import com.cornelmarck.sunnycloud.repository.SyncApiRepository;
import com.cornelmarck.sunnycloud.service.SolaredgeSyncApi;
import com.cornelmarck.sunnycloud.service.SolaredgeSyncApiService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;

@SpringBootTest
@TestPropertySource(locations = "classpath:test.properties")
public class SolaredgeSyncApiIntegrationTest {
    @Autowired
    private DynamoDBMapper dynamoDBMapper;
    @Autowired
    private AmazonDynamoDB amazonDynamoDB;
    @Autowired
    private SolaredgeSyncApiService solaredgeService;
    @Autowired
    private SyncApiRepository syncApiRepository;
    @Value("${solaredge.test.apikey}")
    private String apikey;
    @Value("${solaredge.test.siteid}")
    private String siteId;

    @BeforeEach
    public void init() throws Exception {
        Utils utils = new Utils(amazonDynamoDB, dynamoDBMapper);
        utils.deleteMainTable();
        utils.createMainTable();
        populate();
    }

    @Test
    public void updatePreviousTwoMonths() {
        SolaredgeSyncApi config = syncApiRepository.findBySiteId("1234-2345-wertw4-234").orElseThrow(RuntimeException::new);
        solaredgeService.updateBetween(config, LocalDateTime.now().minusWeeks(6), LocalDateTime.now());
        return;
    }



    public void populate() {
        Site site = new Site();
        site.setName("Test site 1");
        site.setId("1234-2345-wertw4-234");
        site.setOwnerId("john@app.com");
        dynamoDBMapper.save(site);

        SolaredgeSyncApi config = new SolaredgeSyncApi();
        config.setSiteId("1234-2345-wertw4-234");
        config.setApiKey(apikey);
        config.setExternalSiteId(siteId);
        dynamoDBMapper.save(config);
    }
}
