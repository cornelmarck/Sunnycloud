package com.cornelmarck.sunnycloud.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.cornelmarck.sunnycloud.model.DateTimeConverter;
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

    public Optional<Measurement> findBySiteIdAndTimestamp(String siteId, LocalDateTime timestamp) {
        Map<String, AttributeValue> eavMap = new HashMap<>();
        eavMap.put(":v1", new AttributeValue().withS(siteId));
        eavMap.put(":v2", new AttributeValue().withS(dateTimeConverter.convert(timestamp)));

        DynamoDBQueryExpression<Measurement> queryExpression = new DynamoDBQueryExpression<Measurement>()
                .withKeyConditionExpression("Id = :v1 and SortKey = :v2")
                .withExpressionAttributeValues(eavMap);
        List<Measurement> result = dynamoDBMapper.query(Measurement.class, queryExpression);

        if (result.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(result.get(0));
    }

    public List<Measurement> findAllBySiteIdAndTimestampBetween(String siteId, LocalDateTime from, LocalDateTime to) {
        Map<String, AttributeValue> eavMap = new HashMap<>();
        eavMap.put(":v1", new AttributeValue().withS(siteId));
        eavMap.put(":v2", new AttributeValue().withS(dateTimeConverter.convert(from)));
        eavMap.put(":v3", new AttributeValue().withS(dateTimeConverter.convert(to.minusNanos(1000000))));

        DynamoDBQueryExpression<Measurement> queryExpression = new DynamoDBQueryExpression<Measurement>()
                .withKeyConditionExpression("Id = :v1 and SortKey between :v2 and :v3")
                .withExpressionAttributeValues(eavMap);
        return dynamoDBMapper.query(Measurement.class, queryExpression);
    }
}
