package com.cornelmarck.sunnycloud;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.cornelmarck.sunnycloud.model.Site;
import com.cornelmarck.sunnycloud.repository.SiteRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.Optional;

@SpringBootTest(classes = SunnycloudApplication.class)
@TestPropertySource(properties = {"amazon.dynamodb.endpoint=http://localhost:8000/", "amazon.aws.accesskey=test1", "amazon.aws.secretkey=test2" })
public class SiteIntegrationTest {
    @Autowired
    AmazonDynamoDB amazonDynamoDB;
    @Autowired
    DynamoDBMapper dynamoDBMapper;
    @Autowired
    SiteRepository siteRepository;

    @BeforeEach
    public void init() throws Exception {
        Utils utils = new Utils(amazonDynamoDB, dynamoDBMapper);
        utils.deleteMainTable();
        utils.createMainTable();
        utils.populateMainTable();
    }

    @Test
    public void findById() {
        Optional<Site> found = siteRepository.findById("07dd6a84-845e-474d-8c87-a4a3ef21c09e");
        Assertions.assertTrue(found.isPresent());
        Assertions.assertEquals("ex1@hello.com", found.get().getOwnerId());
    }

    @Test
    public void findAllByOwner() {
        List<Site> sites = siteRepository.findByOwner("ex1@hello.com");
        Assertions.assertEquals(2, sites.size());
        Assertions.assertEquals("ex1@hello.com", sites.get(0).getOwnerId());
    }

    @Test
    public void findAll() {
        List<Site> sites = siteRepository.findAll();
        Assertions.assertEquals(4, sites.size());
    }


}
