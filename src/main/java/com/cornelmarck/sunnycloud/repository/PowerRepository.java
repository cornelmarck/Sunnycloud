package com.cornelmarck.sunnycloud.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedList;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.cornelmarck.sunnycloud.model.Power;
import com.cornelmarck.sunnycloud.util.DynamoDBDateTimeConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.*;

@Repository
@RequiredArgsConstructor
public class PowerRepository {
    private final DynamoDBMapper dynamoDBMapper;
    private final DynamoDBDateTimeConverter dynamoConverter;

    public PaginatedList<Power> findAllBySiteIdBetween(String siteId, LocalDateTime from, LocalDateTime to) {
        Map<String, AttributeValue> eavMap = new HashMap<>();
        eavMap.put(":v1", new AttributeValue().withS(siteId));
        eavMap.put(":v2", new AttributeValue().withS(dynamoConverter.convert(from)));
        eavMap.put(":v3", new AttributeValue().withS(dynamoConverter.convert(to.minusSeconds(1))));
        DynamoDBQueryExpression<Power> queryExpression = new DynamoDBQueryExpression<Power>()
                .withKeyConditionExpression("Id = :v1 and SortKey between :v2 and :v3")
                .withExpressionAttributeValues(eavMap);
        return dynamoDBMapper.query(Power.class, queryExpression);
    }

    public Optional<Power> findLatestBySiteId(String siteId) {
        Map<String, AttributeValue> eavMap = new HashMap<>();
        eavMap.put(":v1", new AttributeValue().withS(siteId));
        eavMap.put(":v2", new AttributeValue().withS(dynamoConverter.convert(DynamoDBDateTimeConverter.MAX)));
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

    public Optional<Power> findEarliestBySiteId(String siteId) {
        Map<String, AttributeValue> eavMap = new HashMap<>();
        eavMap.put(":v1", new AttributeValue().withS(siteId));
        eavMap.put(":v2", new AttributeValue().withS(dynamoConverter.convert(DynamoDBDateTimeConverter.MIN)));
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

    public void save(Power power) {
        dynamoDBMapper.save(power);
    }

    public void delete(Power power) {
        dynamoDBMapper.delete(power);
    }
}
