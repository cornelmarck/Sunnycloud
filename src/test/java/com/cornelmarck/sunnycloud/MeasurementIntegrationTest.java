package com.cornelmarck.sunnycloud;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.cornelmarck.sunnycloud.model.DateTimeConverter;
import com.cornelmarck.sunnycloud.model.Measurement;
import com.cornelmarck.sunnycloud.repository.MeasurementRepository;
import com.cornelmarck.sunnycloud.repository.SiteRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
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
    LocalDateTime minTimestamp;
    @Autowired
    LocalDateTime maxTimestamp;

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
                LocalDateTime.parse("2021-08-05T10:12:00.000")
        );
        Assertions.assertTrue(found.isPresent());
        Assertions.assertEquals(4.3, found.get().getPowerOutput());
    }

    @Test
    public void findAllBySiteIdAndTimestampBetween() {
        List<Measurement> result = measurementRepository.findAllBySiteIdAndTimestampBetween(
                "07dd6a84-845e-474d-8c87-a4a3ef21c09e",
                LocalDateTime.parse("2021-08-05T10:22:00.000"),
                LocalDateTime.parse("2021-08-05T10:52:00.000")
        );
        Assertions.assertEquals(3, result.size());
    }

    @Test
    public void findAll() {
        List<Measurement> result = measurementRepository.findAllBySiteIdAndTimestampBetween(
                "07dd6a84-845e-474d-8c87-a4a3ef21c09e",
                LocalDateTime.parse("0000-01-01T00:00:00.000"),
                LocalDateTime.now()
        );
        Assertions.assertEquals(12, result.size());
    }

    @Test
    public void findwithMinMax() {
        List<Measurement> result = measurementRepository.findAllBySiteIdAndTimestampBetween(
                "07dd6a84-845e-474d-8c87-a4a3ef21c09e", minTimestamp, maxTimestamp);
        Assertions.assertEquals(12, result.size());
    }
}
