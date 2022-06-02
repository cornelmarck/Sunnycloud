package com.cornelmarck.sunnycloud;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.cornelmarck.sunnycloud.config.DynamoDBDateTimeConverter;
import com.cornelmarck.sunnycloud.model.Measurement;
import com.cornelmarck.sunnycloud.repository.MeasurementRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@SpringBootTest(classes = SunnycloudApplication.class)
@TestPropertySource(properties = {"amazon.dynamodb.endpoint=http://localhost:8000/", "amazon.aws.accesskey=test1", "amazon.aws.secretkey=test2" })
public class MeasurementIntegrationTest {
    @Autowired
    AmazonDynamoDB amazonDynamoDB;
    @Autowired
    DynamoDBMapper dynamoDBMapper;
    @Autowired
    MeasurementRepository measurementRepository;
    @Autowired
    DynamoDBDateTimeConverter dynamoDBDateTimeConverter;

    @BeforeEach
    public void init() throws Exception {
        Utils utils = new Utils(amazonDynamoDB, dynamoDBMapper);
        utils.deleteMainTable();
        utils.createMainTable();
        utils.populateMainTable();
    }

    @Test
    public void findBySiteIdAndTimestamp() {
        Optional<Measurement> found = measurementRepository.findBySiteIdAndTimestamp(
                "07dd6a84-845e-474d-8c87-a4a3ef21c09e",
                "2021-08-05T10:12:00.000"
        );
        Assertions.assertTrue(found.isPresent());
        Assertions.assertEquals(4.3, found.get().getPowerOutput());
    }

    @Test
    public void findAllBySiteIdAndTimestampBetween() {
        List<Measurement> result = measurementRepository.findAllBySiteIdAndTimestampBetween(
                "07dd6a84-845e-474d-8c87-a4a3ef21c09e",
                Optional.of("2021-08-05T10:22:00.000"), Optional.of("2021-08-05T10:52:00.000")
        );
        Assertions.assertEquals(3, result.size());
    }

    @Test
    public void findAll() {
        List<Measurement> result = measurementRepository.findAllBySiteIdAndTimestampBetween(
                "07dd6a84-845e-474d-8c87-a4a3ef21c09e",
                Optional.of("0000-01-01T00:00:00.000"),
                Optional.of(dynamoDBDateTimeConverter.convert(LocalDateTime.now()))
        );
        Assertions.assertEquals(12, result.size());
    }

    @Test
    public void findWithMinMax() {
        List<Measurement> result = measurementRepository.findAllBySiteIdAndTimestampBetween(
                "07dd6a84-845e-474d-8c87-a4a3ef21c09e", Optional.of(dynamoDBDateTimeConverter.getMinTimestampString()),
                Optional.of(dynamoDBDateTimeConverter.getMaxTimestampString()));
        Assertions.assertEquals(12, result.size());
    }

    @Test
    public void getEarliest() {
        Optional<Measurement> found = measurementRepository.findEarliestBySiteId("07dd6a84-845e-474d-8c87-a4a3ef21c09e");
        Assertions.assertTrue(found.isPresent());
        Assertions.assertEquals(4.3, found.get().getPowerOutput());
    }

    @Test
    public void getLatest() {
        Optional<Measurement> found = measurementRepository.findLatestBySiteId("07dd6a84-845e-474d-8c87-a4a3ef21c09e");
        Assertions.assertTrue(found.isPresent());
        Assertions.assertEquals(0.01, found.get().getPowerOutput());
    }


}
