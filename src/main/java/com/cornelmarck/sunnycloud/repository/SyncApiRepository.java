package com.cornelmarck.sunnycloud.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.cornelmarck.sunnycloud.service.SolaredgeSyncApi;
import com.cornelmarck.sunnycloud.service.SyncApiType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class SyncApiRepository {
    private final DynamoDBMapper dynamoDBMapper;

    public List<SolaredgeSyncApi> findAll() {
        Map<String, AttributeValue> eavMap = new HashMap<>();
        eavMap.put(":v1", new AttributeValue().withS(SyncApiType.SOLAREDGE.name()));

        DynamoDBQueryExpression<SolaredgeSyncApi> queryExpression = new DynamoDBQueryExpression<SolaredgeSyncApi>()
                .withKeyConditionExpression("Id = :v1")
                .withExpressionAttributeValues(eavMap);
        return dynamoDBMapper.query(SolaredgeSyncApi.class, queryExpression);
    }

    public Optional<SolaredgeSyncApi> findBySiteId(String siteId) {
        Map<String, AttributeValue> eavMap = new HashMap<>();
        eavMap.put(":v1", new AttributeValue().withS(SyncApiType.SOLAREDGE.name()));
        eavMap.put(":v2", new AttributeValue().withS(siteId));

        DynamoDBQueryExpression<SolaredgeSyncApi> queryExpression = new DynamoDBQueryExpression<SolaredgeSyncApi>()
                .withKeyConditionExpression("Id = :v1 and SortKey = :v2")
                .withExpressionAttributeValues(eavMap);
        List<SolaredgeSyncApi> found = dynamoDBMapper.query(SolaredgeSyncApi.class, queryExpression);
        if (found.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(found.get(0));
    }
}
