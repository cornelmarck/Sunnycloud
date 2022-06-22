package com.cornelmarck.sunnycloud;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.*;
import com.cornelmarck.sunnycloud.model.Location;
import com.cornelmarck.sunnycloud.model.Power;
import com.cornelmarck.sunnycloud.model.User;
import com.cornelmarck.sunnycloud.model.Site;

import java.time.Instant;
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

    public void createMainTable() throws InterruptedException {
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

    public void createPowerTable() throws InterruptedException {
        DynamoDB dynamoDB = new DynamoDB(amazonDynamoDB);

        List<AttributeDefinition> attributeDefinitions = new ArrayList<>();
        attributeDefinitions.add(new AttributeDefinition().withAttributeName("Id").withAttributeType(ScalarAttributeType.S));
        attributeDefinitions.add(new AttributeDefinition().withAttributeName("SortKey").withAttributeType(ScalarAttributeType.S));

        List<KeySchemaElement> keySchema = new ArrayList<>();
        keySchema.add(new KeySchemaElement().withAttributeName("Id").withKeyType(KeyType.HASH));
        keySchema.add(new KeySchemaElement().withAttributeName("SortKey").withKeyType(KeyType.RANGE));

        CreateTableRequest request = new CreateTableRequest()
                .withTableName("Power")
                .withKeySchema(keySchema)
                .withAttributeDefinitions(attributeDefinitions)
                .withProvisionedThroughput(new ProvisionedThroughput()
                        .withReadCapacityUnits(1L)
                        .withWriteCapacityUnits(1L));

        Table table = dynamoDB.createTable(request);
        table.waitForActive();
    }

    public void deleteTables() {
        DynamoDB dynamoDB = new DynamoDB(amazonDynamoDB);
        for (String table : amazonDynamoDB.listTables().getTableNames()) {
            dynamoDB.getTable(table).delete();
        }
    }

    public void populateMainTable() {
        User user1 = new User();
        user1.setEmailAddress("ex1@hello.com");
        user1.setName("Harry Styles");
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
        site.setTimeZone("UTC");
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
        site.setTimeZone("UTC");
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
        site.setTimeZone("CET");
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
        site.setTimeZone("CET");
        dynamoDBMapper.save(site);
        Site site4 = site;
    }
}
