package com.cornelmarck.sunnycloud.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.cornelmarck.sunnycloud.model.Site;
import com.cornelmarck.sunnycloud.model.SyncApiType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class SiteRepository {
    private final DynamoDBMapper dynamoDBMapper;

    public Optional<Site> findById(String siteId) {
        Map<String, AttributeValue> eavMap = new HashMap<>();
        eavMap.put(":v1", new AttributeValue().withS(siteId));
        eavMap.put(":v2", new AttributeValue().withS("Site"));

        DynamoDBQueryExpression<Site> queryExpression = new DynamoDBQueryExpression<Site>()
                .withKeyConditionExpression("Id = :v1 and SortKey = :v2")
                .withExpressionAttributeValues(eavMap);
        List<Site> found = dynamoDBMapper.query(Site.class, queryExpression);

        if (found.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(found.get(0));
    }

    public List<Site> findByOwner(String ownerEmailAddress) {
        Map<String, AttributeValue> eavMap = new HashMap<>();
        eavMap.put(":v1", new AttributeValue().withS(ownerEmailAddress));

        DynamoDBQueryExpression<Site> queryExpression = new DynamoDBQueryExpression<Site>()
                .withIndexName("SiteOwnerIndex")
                .withConsistentRead(false)
                .withKeyConditionExpression("OwnerId = :v1")
                .withExpressionAttributeValues(eavMap);
        return dynamoDBMapper.query(Site.class, queryExpression);
    }

    public List<Site> findAll() {
        Map<String, AttributeValue> eavMap = new HashMap<>();
        eavMap.put(":v1", new AttributeValue().withS("Site"));

        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression ()
                .withFilterExpression("SortKey = :v1")
                .withExpressionAttributeValues(eavMap);
        return dynamoDBMapper.scan(Site.class, scanExpression);
    }

    public List<Site> findAllBySyncApiType(SyncApiType type) {
        Map<String, AttributeValue> eavMap = new HashMap<>();
        eavMap.put(":v1", new AttributeValue().withS(type.name()));

        DynamoDBQueryExpression<Site> queryExpression = new DynamoDBQueryExpression<Site>()
                .withIndexName("SyncApiTypeIndex")
                .withKeyConditionExpression("SyncApiType = :v1")
                .withConsistentRead(false)
                .withExpressionAttributeValues(eavMap);
        return dynamoDBMapper.query(Site.class, queryExpression);
    }

    public void delete(String siteId) {
        findById(siteId).ifPresent(dynamoDBMapper::delete);
    }

    public void save(Site newSite) {
        dynamoDBMapper.save(newSite);
    }
}

