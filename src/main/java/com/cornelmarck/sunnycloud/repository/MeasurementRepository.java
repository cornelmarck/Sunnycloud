package com.cornelmarck.sunnycloud.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.cornelmarck.sunnycloud.model.Measurement;
import com.cornelmarck.sunnycloud.config.DynamoDBDateTimeConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.*;

@Repository
@RequiredArgsConstructor
public class MeasurementRepository {
    private final DynamoDBMapper dynamoDBMapper;
    private final DynamoDBDateTimeConverter dynamoDBDateTimeConverter;

    public Optional<Measurement> findBySiteIdAndTimestamp(String siteId, String timestamp) {
        Map<String, AttributeValue> eavMap = new HashMap<>();
        eavMap.put(":v1", new AttributeValue().withS(siteId));
        eavMap.put(":v2", new AttributeValue().withS(timestamp));

        DynamoDBQueryExpression<Measurement> queryExpression = new DynamoDBQueryExpression<Measurement>()
                .withKeyConditionExpression("Id = :v1 and SortKey = :v2")
                .withExpressionAttributeValues(eavMap);
        List<Measurement> result = dynamoDBMapper.query(Measurement.class, queryExpression);

        if (result.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(result.get(0));
    }

    public List<Measurement> findAllBySiteIdAndTimestampBetween(String siteId, Optional<String> from, Optional<String> to) {
        Map<String, AttributeValue> eavMap = new HashMap<>();
        String start = from.orElseGet(dynamoDBDateTimeConverter::getMinTimestampString);
        LocalDateTime end = to.map(dynamoDBDateTimeConverter::unconvert).orElse(dynamoDBDateTimeConverter.getMaxTimestamp());

        eavMap.put(":v1", new AttributeValue().withS(siteId));
        eavMap.put(":v2", new AttributeValue().withS(start));
        eavMap.put(":v3", new AttributeValue().withS(dynamoDBDateTimeConverter.convert(end.minusSeconds(1))));

        DynamoDBQueryExpression<Measurement> queryExpression = new DynamoDBQueryExpression<Measurement>()
                .withKeyConditionExpression("Id = :v1 and SortKey between :v2 and :v3")
                .withExpressionAttributeValues(eavMap);
        return dynamoDBMapper.query(Measurement.class, queryExpression);
    }

    public Optional<Measurement> findEarliestBySiteId(String siteId) {
        Map<String, AttributeValue> eavMap = new HashMap<>();
        eavMap.put(":v1", new AttributeValue().withS(siteId));
        eavMap.put(":v2", new AttributeValue().withS(dynamoDBDateTimeConverter.getMinTimestampString()));

        DynamoDBQueryExpression<Measurement> queryExpression = new DynamoDBQueryExpression<Measurement>()
                .withKeyConditionExpression("Id = :v1 and SortKey >= :v2")
                .withExpressionAttributeValues(eavMap)
                .withScanIndexForward(true)
                .withLimit(1);
        List<Measurement> found = dynamoDBMapper.query(Measurement.class, queryExpression);
        if (found.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(found.get(0));
    }

    public Optional<Measurement> findLatestBySiteId(String siteId) {
        Map<String, AttributeValue> eavMap = new HashMap<>();
        eavMap.put(":v1", new AttributeValue().withS(siteId));
        eavMap.put(":v2", new AttributeValue().withS(dynamoDBDateTimeConverter.getMaxTimestampString()));

        DynamoDBQueryExpression<Measurement> queryExpression = new DynamoDBQueryExpression<Measurement>()
                .withKeyConditionExpression("Id = :v1 and SortKey < :v2")
                .withExpressionAttributeValues(eavMap)
                .withScanIndexForward(false)
                .withLimit(1);
        List<Measurement> found = dynamoDBMapper.query(Measurement.class, queryExpression);
        if (found.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(found.get(0));
    }

    public void batchSave(Collection<Measurement> measurements) {
        dynamoDBMapper.batchSave(measurements);
    }

    public void save(Measurement measurement) {
        dynamoDBMapper.save(measurement);
    }

}
