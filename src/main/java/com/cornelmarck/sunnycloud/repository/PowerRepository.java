package com.cornelmarck.sunnycloud.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.cornelmarck.sunnycloud.model.Power;
import com.cornelmarck.sunnycloud.config.DynamoDBDateTimeConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.*;

@Repository
@RequiredArgsConstructor
public class PowerRepository {
    private final DynamoDBMapper dynamoDBMapper;
    private final DynamoDBDateTimeConverter dynamoDBDateTimeConverter;

    public Optional<Power> findBySiteIdAndTimestamp(String siteId, String timestamp) {
        Map<String, AttributeValue> eavMap = new HashMap<>();
        eavMap.put(":v1", new AttributeValue().withS(siteId));
        eavMap.put(":v2", new AttributeValue().withS(timestamp));

        DynamoDBQueryExpression<Power> queryExpression = new DynamoDBQueryExpression<Power>()
                .withKeyConditionExpression("Id = :v1 and SortKey = :v2")
                .withExpressionAttributeValues(eavMap);
        List<Power> result = dynamoDBMapper.query(Power.class, queryExpression);

        if (result.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(result.get(0));
    }

    public List<Power> findAllBySiteIdAndTimestampBetween(String siteId, Optional<String> from, Optional<String> to) {
        Map<String, AttributeValue> eavMap = new HashMap<>();
        String start = from.orElseGet(dynamoDBDateTimeConverter::getMinTimestampString);
        LocalDateTime end = to.map(dynamoDBDateTimeConverter::unconvert).orElse(dynamoDBDateTimeConverter.getMaxTimestamp());

        eavMap.put(":v1", new AttributeValue().withS(siteId));
        eavMap.put(":v2", new AttributeValue().withS(start));
        eavMap.put(":v3", new AttributeValue().withS(dynamoDBDateTimeConverter.convert(end.minusSeconds(1))));

        DynamoDBQueryExpression<Power> queryExpression = new DynamoDBQueryExpression<Power>()
                .withKeyConditionExpression("Id = :v1 and SortKey between :v2 and :v3")
                .withExpressionAttributeValues(eavMap);
        return dynamoDBMapper.query(Power.class, queryExpression);
    }

    public Optional<Power> findEarliestBySiteId(String siteId) {
        Map<String, AttributeValue> eavMap = new HashMap<>();
        eavMap.put(":v1", new AttributeValue().withS(siteId));
        eavMap.put(":v2", new AttributeValue().withS(dynamoDBDateTimeConverter.getMinTimestampString()));

        DynamoDBQueryExpression<Power> queryExpression = new DynamoDBQueryExpression<Power>()
                .withKeyConditionExpression("Id = :v1 and SortKey >= :v2")
                .withExpressionAttributeValues(eavMap)
                .withScanIndexForward(true)
                .withLimit(1);
        List<Power> found = dynamoDBMapper.query(Power.class, queryExpression);
        if (found.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(found.get(0));
    }

    public Optional<Power> findLatestBySiteId(String siteId) {
        Map<String, AttributeValue> eavMap = new HashMap<>();
        eavMap.put(":v1", new AttributeValue().withS(siteId));
        eavMap.put(":v2", new AttributeValue().withS(dynamoDBDateTimeConverter.getMaxTimestampString()));

        DynamoDBQueryExpression<Power> queryExpression = new DynamoDBQueryExpression<Power>()
                .withKeyConditionExpression("Id = :v1 and SortKey < :v2")
                .withExpressionAttributeValues(eavMap)
                .withScanIndexForward(false)
                .withLimit(1);
        List<Power> found = dynamoDBMapper.query(Power.class, queryExpression);
        if (found.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(found.get(0));
    }

    public void batchSave(Collection<Power> powerList) {
        dynamoDBMapper.batchSave(powerList);
    }

    public void save(Power power) {
        dynamoDBMapper.save(power);
    }

}
