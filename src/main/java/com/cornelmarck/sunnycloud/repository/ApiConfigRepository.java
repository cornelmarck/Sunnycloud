package com.cornelmarck.sunnycloud.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.cornelmarck.sunnycloud.model.ApiConfigWrapper;
import com.cornelmarck.sunnycloud.model.SyncApiType;
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

    public List<ApiConfigWrapper> findAllByType(SyncApiType type) {
        Map<String, AttributeValue> eavMap = new HashMap<>();
        eavMap.put(":v1", new AttributeValue().withS(type.name()));

        DynamoDBQueryExpression<ApiConfigWrapper> queryExpression = new DynamoDBQueryExpression<ApiConfigWrapper>()
                .withIndexName("SyncApiTypeIndex")
                .withKeyConditionExpression("SyncApiType = :v1")
                .withConsistentRead(false)
                .withExpressionAttributeValues(eavMap);
        return dynamoDBMapper.query(ApiConfigWrapper.class, queryExpression);
    }

    public void save(ApiConfigWrapper wrapper) {
        dynamoDBMapper.save(wrapper);
    }

    public Optional<ApiConfigWrapper> findBySiteId(String siteId) {
        Map<String, AttributeValue> eavMap = new HashMap<>();
        eavMap.put(":v1", new AttributeValue().withS(siteId));
        eavMap.put(":v2", new AttributeValue().withS("SyncApi"));

        DynamoDBQueryExpression<ApiConfigWrapper> queryExpression = new DynamoDBQueryExpression<ApiConfigWrapper>()
                .withKeyConditionExpression("Id = :v1 and SortKey = :v2")
                .withExpressionAttributeValues(eavMap);
        List<ApiConfigWrapper> found = dynamoDBMapper.query(ApiConfigWrapper.class, queryExpression);
        if (found.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(found.get(0));
    }

    public void deleteBySiteId(String siteId) {
        findBySiteId(siteId).ifPresent(dynamoDBMapper::delete);
    }
}
