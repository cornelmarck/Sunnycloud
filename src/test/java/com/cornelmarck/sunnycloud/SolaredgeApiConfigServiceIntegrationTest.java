package com.cornelmarck.sunnycloud;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.cornelmarck.sunnycloud.model.Site;
import com.cornelmarck.sunnycloud.model.SolaredgeApiConfig;
import com.cornelmarck.sunnycloud.service.SolaredgeSyncApiService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(locations = "classpath:test.properties")
public class SolaredgeApiConfigServiceIntegrationTest {
    @Autowired
    private DynamoDBMapper dynamoDBMapper;
    @Autowired
    private AmazonDynamoDB amazonDynamoDB;
    @Autowired
    private SolaredgeSyncApiService solaredgeService;
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
    public void getSyncService() {

    }



    public void populate() {
        Site site = new Site();
        site.setName("Test site 1");
        site.setOwnerId("john@app.com");

        SolaredgeApiConfig config = new SolaredgeApiConfig();
        config.setApiKey(apikey);
        config.setExternalSiteId(siteId);
    }
}
