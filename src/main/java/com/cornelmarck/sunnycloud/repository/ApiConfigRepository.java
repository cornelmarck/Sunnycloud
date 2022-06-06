package com.cornelmarck.sunnycloud.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.cornelmarck.sunnycloud.service.SolaredgeApiConfig;
import com.cornelmarck.sunnycloud.service.SyncApiType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ApiConfigRepository {
    private final DynamoDBMapper dynamoDBMapper;

    public List<SolaredgeApiConfig> findAll() {
        Map<String, AttributeValue> eavMap = new HashMap<>();
        eavMap.put(":v1", new AttributeValue().withS(SyncApiType.SOLAREDGE.name()));

        DynamoDBQueryExpression<SolaredgeApiConfig> queryExpression = new DynamoDBQueryExpression<SolaredgeApiConfig>()
                .withKeyConditionExpression("Id = :v1")
                .withExpressionAttributeValues(eavMap);
        return dynamoDBMapper.query(SolaredgeApiConfig.class, queryExpression);
    }

    public Optional<SolaredgeApiConfig> findBySiteId(String siteId) {
        Map<String, AttributeValue> eavMap = new HashMap<>();
        eavMap.put(":v1", new AttributeValue().withS(SyncApiType.SOLAREDGE.name()));
        eavMap.put(":v2", new AttributeValue().withS(siteId));

        DynamoDBQueryExpression<SolaredgeApiConfig> queryExpression = new DynamoDBQueryExpression<SolaredgeApiConfig>()
                .withKeyConditionExpression("Id = :v1 and SortKey = :v2")
                .withExpressionAttributeValues(eavMap);
        List<SolaredgeApiConfig> found = dynamoDBMapper.query(SolaredgeApiConfig.class, queryExpression);
        if (found.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(found.get(0));
    }
}
