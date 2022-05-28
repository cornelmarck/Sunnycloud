package com.cornelmarck.sunnycloud.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.cornelmarck.sunnycloud.model.Measurement;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class MeasurementRepository {
    private final DynamoDBMapper dynamoDBMapper;
    private final DateTimeConverter dateTimeConverter;

    public MeasurementRepository(DynamoDBMapper dynamoDBMapper, DateTimeConverter dateTimeConverter) {
        this.dynamoDBMapper = dynamoDBMapper;
        this.dateTimeConverter = dateTimeConverter;
    }

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
        String start = from.orElseGet(dateTimeConverter::getMinTimestampString);
        LocalDateTime end = to.map(dateTimeConverter::unconvert).orElse(dateTimeConverter.getMaxTimestamp());

        eavMap.put(":v1", new AttributeValue().withS(siteId));
        eavMap.put(":v2", new AttributeValue().withS(start));
        eavMap.put(":v3", new AttributeValue().withS(dateTimeConverter.convert(end.minusNanos(1000000))));

        DynamoDBQueryExpression<Measurement> queryExpression = new DynamoDBQueryExpression<Measurement>()
                .withKeyConditionExpression("Id = :v1 and SortKey between :v2 and :v3")
                .withExpressionAttributeValues(eavMap);
        return dynamoDBMapper.query(Measurement.class, queryExpression);
    }

    public Optional<Measurement> findEarliestBySiteId(String siteId) {
        Map<String, AttributeValue> eavMap = new HashMap<>();
        eavMap.put(":v1", new AttributeValue().withS(siteId));
        eavMap.put(":v2", new AttributeValue().withS(dateTimeConverter.getMinTimestampString()));

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
        eavMap.put(":v2", new AttributeValue().withS(dateTimeConverter.getMaxTimestampString()));

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
}
