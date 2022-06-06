package com.cornelmarck.sunnycloud;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.cornelmarck.sunnycloud.config.DynamoDBInstantConverter;
import com.cornelmarck.sunnycloud.model.Power;
import com.cornelmarck.sunnycloud.repository.PowerRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@SpringBootTest(classes = SunnycloudApplication.class)
@TestPropertySource(properties = {"amazon.dynamodb.endpoint=http://localhost:8000/", "amazon.aws.accesskey=test1", "amazon.aws.secretkey=test2" })
public class PowerIntegrationTest {
    @Autowired
    AmazonDynamoDB amazonDynamoDB;
    @Autowired
    DynamoDBMapper dynamoDBMapper;
    @Autowired
    PowerRepository powerRepository;
    @Autowired
    DynamoDBInstantConverter dynamoDBInstantConverter;

    @BeforeEach
    public void init() throws Exception {
        Utils utils = new Utils(amazonDynamoDB, dynamoDBMapper);
        utils.deleteMainTable();
        utils.createMainTable();
        utils.populateMainTable();
    }

    @Test
    public void findBySiteIdAndTimestamp() {
        Optional<Power> found = powerRepository.findBySiteIdAndTimestamp(
                "07dd6a84-845e-474d-8c87-a4a3ef21c09e",
                Instant.parse("2021-08-05T10:12:00Z")
        );
        Assertions.assertTrue(found.isPresent());
        Assertions.assertEquals(4.3, found.get().getPowerOutput());
    }

    @Test
    public void findAllBySiteIdAndTimestampBetween() {
        List<Power> result = powerRepository.findAllBySiteIdAndTimestampBetween(
                "07dd6a84-845e-474d-8c87-a4a3ef21c09e",
                Instant.parse("2021-08-05T10:22:00"), Instant.parse("2021-08-05T10:52:00Z")
        );
        Assertions.assertEquals(3, result.size());
    }

    @Test
    public void findAll() {
        List<Power> result = powerRepository.findAllBySiteIdAndTimestampBetween(
                "07dd6a84-845e-474d-8c87-a4a3ef21c09e",
                Instant.parse("0000-01-01T00:00:00Z"),
                Instant.parse(dynamoDBInstantConverter.convert(Instant.now()))
        );
        Assertions.assertEquals(12, result.size());
    }

    @Test
    public void findWithMinMax() {
        List<Power> result = powerRepository.findAllBySiteIdAndTimestampBetween(
                "07dd6a84-845e-474d-8c87-a4a3ef21c09e", DynamoDBInstantConverter.MIN, DynamoDBInstantConverter.MAX);
        Assertions.assertEquals(12, result.size());
    }

    @Test
    public void getEarliest() {
        Optional<Power> found = powerRepository.findEarliestBySiteId("07dd6a84-845e-474d-8c87-a4a3ef21c09e");
        Assertions.assertTrue(found.isPresent());
        Assertions.assertEquals(4.3, found.get().getPowerOutput());
    }

    @Test
    public void getLatest() {
        Optional<Power> found = powerRepository.findLatestBySiteId("07dd6a84-845e-474d-8c87-a4a3ef21c09e");
        Assertions.assertTrue(found.isPresent());
        Assertions.assertEquals(0.01, found.get().getPowerOutput());
    }


}
