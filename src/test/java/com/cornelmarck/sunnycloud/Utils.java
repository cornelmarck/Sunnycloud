package com.cornelmarck.sunnycloud;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.*;
import com.cornelmarck.sunnycloud.model.Location;
import com.cornelmarck.sunnycloud.model.Measurement;
import com.cornelmarck.sunnycloud.model.User;
import com.cornelmarck.sunnycloud.model.Site;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Utils {
    private final AmazonDynamoDB amazonDynamoDB;
    private final DynamoDBMapper dynamoDBMapper;

    public Utils(AmazonDynamoDB amazonDynamoDB, DynamoDBMapper dynamoDBMapper) {
        this.amazonDynamoDB = amazonDynamoDB;
        this.dynamoDBMapper = dynamoDBMapper;
    }

    public void createMainTable() throws Exception {
        DynamoDB dynamoDB = new DynamoDB(amazonDynamoDB);

        List<AttributeDefinition> attributeDefinitions = new ArrayList<>();
        attributeDefinitions.add(new AttributeDefinition().withAttributeName("Id").withAttributeType(ScalarAttributeType.S));
        attributeDefinitions.add(new AttributeDefinition().withAttributeName("SortKey").withAttributeType(ScalarAttributeType.S));

        List<KeySchemaElement> keySchema = new ArrayList<>();
        keySchema.add(new KeySchemaElement().withAttributeName("Id").withKeyType(KeyType.HASH));
        keySchema.add(new KeySchemaElement().withAttributeName("SortKey").withKeyType(KeyType.RANGE));

        GlobalSecondaryIndex gsi1 = new GlobalSecondaryIndex()
                .withIndexName("SiteOwnerIndex")
                .withKeySchema(new KeySchemaElement().withAttributeName("OwnerId").withKeyType(KeyType.HASH))
                .withProvisionedThroughput(new ProvisionedThroughput()
                        .withReadCapacityUnits(1L)
                        .withWriteCapacityUnits(1L))
                .withProjection(new Projection().withProjectionType(ProjectionType.ALL));
        attributeDefinitions.add(new AttributeDefinition().withAttributeName("OwnerId").withAttributeType(ScalarAttributeType.S));

        CreateTableRequest request = new CreateTableRequest()
                .withTableName("Main")
                .withKeySchema(keySchema)
                .withGlobalSecondaryIndexes(gsi1)
                .withAttributeDefinitions(attributeDefinitions)
                .withProvisionedThroughput(new ProvisionedThroughput()
                        .withReadCapacityUnits(1L)
                        .withWriteCapacityUnits(1L));

        Table table = dynamoDB.createTable(request);
        table.waitForActive();
    }

    public void deleteMainTable() {
        DynamoDB dynamoDB = new DynamoDB(amazonDynamoDB);
        try {
            Table main = dynamoDB.getTable("Main");
            main.delete();
        }
        catch (RuntimeException ignored) {}
    }

    public void populateMainTable() {
        User user1 = new User();
        user1.setEmailAddress("ex1@hello.com");
        user1.setName("Harry Style");
        user1.setMobilePhoneNumber("1234 5678");
        User user2 = new User();
        user2.setEmailAddress("ex2@hello.com");
        user2.setMobilePhoneNumber("2345 6789");
        user2.setName("Joanna Doe");
        User user3 = new User();
        user3.setEmailAddress("ex3@asdf.com");
        user3.setMobilePhoneNumber("+1 1234 5678");
        user3.setName("Samantha Smith");
        dynamoDBMapper.batchSave(List.of(user1, user2, user3));

        Site site = new Site();
        site.setId("07dd6a84-845e-474d-8c87-a4a3ef21c09e");
        site.setOwnerId(user1.getEmailAddress());
        site.setName("Back garden PV");
        site.setPeakPower(3.67);
        site.setTiltAngle(21.4);
        site.setAzimuthAngle(0.12);
        site.setLocation(new Location());
        site.getLocation().setLatitude(51.5072);
        site.getLocation().setLongitude(-0.12755);
        site.getLocation().setCity("London");
        site.getLocation().setAddress1("34 Windsor Drive");
        site.getLocation().setZipCode("W3 5CX");
        site.getLocation().setCountry("United Kingdom");
        site.getLocation().setCountryCode("UK");
        site.getLocation().setTimeZone("UTC");
        dynamoDBMapper.save(site);
        Site site1 = site;

        site = new Site();
        site.setOwnerId(user1.getEmailAddress());
        site.setName("Front roof installation");
        site.setPeakPower(24.67);
        site.setTiltAngle(34.34);
        site.setAzimuthAngle(32.12);
        site.setLocation(new Location());
        site.getLocation().setLatitude(52.5072);
        site.getLocation().setLongitude(-0.30755);
        site.getLocation().setCity("Guildford");
        site.getLocation().setAddress1("12 Queen's Court");
        site.getLocation().setAddress2("Uxbridge Road");
        site.getLocation().setZipCode("SW92 AB1");
        site.getLocation().setCountry("United Kingdom");
        site.getLocation().setCountryCode("UK");
        site.getLocation().setTimeZone("UTC");
        dynamoDBMapper.save(site);
        Site site2 = site;

        site = new Site();
        site.setOwnerId(user2.getEmailAddress());
        site.setName("Installation");
        site.setPeakPower(2.467);
        site.setTiltAngle(45);
        site.setAzimuthAngle(0.34);
        site.setLocation(new Location());
        site.getLocation().setLatitude(55.5072);
        site.getLocation().setLongitude(-5.30755);
        site.getLocation().setCity("Manchester");
        site.getLocation().setAddress1("45 Small Heath");
        site.getLocation().setZipCode("BEA 5WS");
        site.getLocation().setCountry("United Kingdom");
        site.getLocation().setCountryCode("UK");
        site.getLocation().setTimeZone("UTC");
        dynamoDBMapper.save(site);
        Site site3 = site;

        site = new Site();
        site.setOwnerId(user3.getEmailAddress());
        site.setName("Installation");
        site.setPeakPower(2.467);
        site.setTiltAngle(45);
        site.setAzimuthAngle(0.34);
        site.setLocation(new Location());
        site.getLocation().setLatitude(50.5072);
        site.getLocation().setLongitude(12.30755);
        site.getLocation().setCity("Dortmund");
        site.getLocation().setAddress1("Friedrichsstraße 45");
        site.getLocation().setZipCode("ASDF 23");
        site.getLocation().setCountry("Germany");
        site.getLocation().setCountryCode("DE");
        site.getLocation().setTimeZone("UTC+1");
        dynamoDBMapper.save(site);
        Site site4 = site;

        LocalDateTime base = LocalDateTime.parse("2021-08-05T10:12:00.000");
        dynamoDBMapper.save(new Measurement(site1.getId(), base.plusMinutes(0), 4.3));
        dynamoDBMapper.save(new Measurement(site1.getId(), base.plusMinutes(10), 3.3));
        dynamoDBMapper.save(new Measurement(site1.getId(), base.plusMinutes(20), 4.6));
        dynamoDBMapper.save(new Measurement(site1.getId(), base.plusMinutes(30), 5.1));
        dynamoDBMapper.save(new Measurement(site1.getId(), base.plusMinutes(40), 2.8));
        dynamoDBMapper.save(new Measurement(site1.getId(), base.plusMinutes(50), 0.0));
        dynamoDBMapper.save(new Measurement(site1.getId(), base.plusHours(0), 4.31));
        dynamoDBMapper.save(new Measurement(site1.getId(), base.plusHours(10), 3.31));
        dynamoDBMapper.save(new Measurement(site1.getId(), base.plusHours(20), 4.61));
        dynamoDBMapper.save(new Measurement(site1.getId(), base.plusHours(30), 5.11));
        dynamoDBMapper.save(new Measurement(site1.getId(), base.plusHours(40), 2.81));
        dynamoDBMapper.save(new Measurement(site1.getId(), base.plusHours(50), 0.01));

        base = LocalDateTime.parse("2021-08-05T10:00:00.000");
        dynamoDBMapper.save(new Measurement(site2.getId(), base.plusMinutes(0), 2));
        dynamoDBMapper.save(new Measurement(site2.getId(), base.plusMinutes(5), 3));
        dynamoDBMapper.save(new Measurement(site2.getId(), base.plusMinutes(8), 1.3));
        dynamoDBMapper.save(new Measurement(site2.getId(), base.plusMinutes(10), 1.2));
        dynamoDBMapper.save(new Measurement(site2.getId(), base.plusMinutes(13), 1.1));
        dynamoDBMapper.save(new Measurement(site2.getId(), base.plusMinutes(25), 0.9));

        base = LocalDateTime.parse("2021-08-05T10:12:00.000");
        dynamoDBMapper.save(new Measurement(site3.getId(), base.plusMinutes(0), 7.3));
        dynamoDBMapper.save(new Measurement(site3.getId(), base.plusMinutes(10), 4.3));

        base = LocalDateTime.parse("2014-02-28T13:08:10");
        dynamoDBMapper.save(new Measurement(site4.getId(), base.plusMinutes(0), 4.3));
        dynamoDBMapper.save(new Measurement(site4.getId(), base.plusDays(10), 8.3));
        dynamoDBMapper.save(new Measurement(site4.getId(), base.plusDays(8), 0.1));
    }
}
