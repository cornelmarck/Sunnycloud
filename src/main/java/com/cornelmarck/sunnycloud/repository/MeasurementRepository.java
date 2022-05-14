package com.cornelmarck.sunnycloud.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.cornelmarck.sunnycloud.model.Measurement;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class MeasurementRepository {
    private final DynamoDBMapper dynamoDBMapper;

    public MeasurementRepository(DynamoDBMapper dynamoDBMapper) {
        this.dynamoDBMapper = dynamoDBMapper;
    }

    public Optional<Measurement> findBySiteIdAndTimestamp(String siteId, String timestamp) {
        return Optional.empty();
    }
}
