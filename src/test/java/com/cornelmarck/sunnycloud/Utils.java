package com.cornelmarck.sunnycloud;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.*;
import com.cornelmarck.sunnycloud.model.Power;
import com.cornelmarck.sunnycloud.model.Site;
import com.cornelmarck.sunnycloud.model.User;
import com.cornelmarck.sunnycloud.repository.UserRepository;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest(classes = SunnycloudApplication.class)
@TestPropertySource(properties = {"amazon.dynamodb.endpoint=http://localhost:8000/", "amazon.aws.accesskey=test1", "amazon.aws.secretkey=test2" })
public class Utils {
    private AmazonDynamoDB amazonDynamoDB;
    private DynamoDBMapper dynamoDBMapper;

    public Utils(AmazonDynamoDB amazonDynamoDB, DynamoDBMapper dynamoDBMapper) {
        this.amazonDynamoDB = amazonDynamoDB;
        this.dynamoDBMapper = dynamoDBMapper;
    }

    public void createMainTable() throws Exception {
        DynamoDB dynamoDB = new DynamoDB(amazonDynamoDB);

        List<AttributeDefinition> attributeDefinitions = new ArrayList<>();
        attributeDefinitions.add(new AttributeDefinition().withAttributeName("EmailAddress").withAttributeType("S"));
        attributeDefinitions.add(new AttributeDefinition().withAttributeName("Type_SiteId_Timestamp").withAttributeType(ScalarAttributeType.S));

        List<KeySchemaElement> keySchema = new ArrayList<>();
        keySchema.add(new KeySchemaElement().withAttributeName("EmailAddress").withKeyType(KeyType.HASH));
        keySchema.add(new KeySchemaElement().withAttributeName("Type_SiteId_Timestamp").withKeyType(KeyType.RANGE));

        GlobalSecondaryIndex gsi1 = new GlobalSecondaryIndex()
                .withIndexName("MobileNumberIndex")
                .withKeySchema(new KeySchemaElement().withAttributeName("MobileNumber").withKeyType(KeyType.HASH))
                .withProvisionedThroughput(new ProvisionedThroughput()
                        .withReadCapacityUnits(1L)
                        .withWriteCapacityUnits(1L))
                .withProjection(new Projection().withProjectionType(ProjectionType.KEYS_ONLY));
        attributeDefinitions.add(new AttributeDefinition().withAttributeName("MobileNumber").withAttributeType(ScalarAttributeType.S));

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
        catch (ResourceNotFoundException ignored) {}
    }

    public void populateMainTable() {
        User user1 = new User("ex1@hello.com");
        user1.setMobileNumber("1234 5678");
        User user2 = new User("ex2@hello.com");
        user2.setMobileNumber("2345 6789");
        User user3 = new User("ex3@hello.com");
        user3.setMobileNumber("+1 1234 5678");
        dynamoDBMapper.batchSave(List.of(user1, user2, user3));


        Site site1 = new Site(user1.getId());
        site1.setName("Back garden PV");
        site1.setCity("London");
        site1.setAddress("34 Windsor Drive");
        site1.setZip("W3 5CX");
        site1.setCountry("United Kingdom");
        site1.setCountryCode("UK");
        site1.setTimeZone(0);
        dynamoDBMapper.save(site1);

        Site site2 = new Site(user1.getId());
        site2.setName("Front roof installation");
        site2.setCity("London");
        site2.setAddress("34 Windsor Drive");
        site2.setZip("W3 5CX");
        site2.setCountry("United Kingdom");
        site2.setCountryCode("UK");
        site2.setTimeZone(0);
        dynamoDBMapper.save(site2);

        Site site3 = new Site(user2.getId());
        site3.setName("Installation");
        site3.setCity("Manchester");
        site3.setAddress("45 Small Heath");
        site3.setZip("BEA 5WS");
        site3.setCountry("United Kingdom");
        site3.setCountryCode("UK");
        site3.setTimeZone(0);
        dynamoDBMapper.save(site3);

        Site site4 = new Site(user3.getId());
        site4.setName("PV Installation am Dach");
        site4.setCity("Dortmund");
        site4.setAddress("Friedrichsstraße 45");
        site4.setZip("ASDF 23");
        site4.setCountry("Germany");
        site4.setCountryCode("DE");
        site4.setTimeZone(1);
        dynamoDBMapper.save(site4);

        LocalDateTime base = LocalDateTime.parse("2021-08-05T10:12:00");
        dynamoDBMapper.save(new Power(user1.getId(), site1.getSiteId(), base.plusMinutes(0), 4.3));
        dynamoDBMapper.save(new Power(user1.getId(), site1.getSiteId(), base.plusMinutes(10), 3.3));
        dynamoDBMapper.save(new Power(user1.getId(), site1.getSiteId(), base.plusMinutes(20), 4.6));
        dynamoDBMapper.save(new Power(user1.getId(), site1.getSiteId(), base.plusMinutes(30), 5.1));
        dynamoDBMapper.save(new Power(user1.getId(), site1.getSiteId(), base.plusMinutes(40), 2.8));
        dynamoDBMapper.save(new Power(user1.getId(), site1.getSiteId(), base.plusMinutes(50), 0.0));
        dynamoDBMapper.save(new Power(user1.getId(), site1.getSiteId(), base.plusHours(0), 4.31));
        dynamoDBMapper.save(new Power(user1.getId(), site1.getSiteId(), base.plusHours(10), 3.31));
        dynamoDBMapper.save(new Power(user1.getId(), site1.getSiteId(), base.plusHours(20), 4.61));
        dynamoDBMapper.save(new Power(user1.getId(), site1.getSiteId(), base.plusHours(30), 5.11));
        dynamoDBMapper.save(new Power(user1.getId(), site1.getSiteId(), base.plusHours(40), 2.81));
        dynamoDBMapper.save(new Power(user1.getId(), site1.getSiteId(), base.plusHours(50), 0.01));

        base = LocalDateTime.parse("2021-08-05T10:00:00");
        dynamoDBMapper.save(new Power(user1.getId(), site2.getSiteId(), base.plusMinutes(0), 2));
        dynamoDBMapper.save(new Power(user1.getId(), site2.getSiteId(), base.plusMinutes(5), 3));
        dynamoDBMapper.save(new Power(user1.getId(), site2.getSiteId(), base.plusMinutes(8), 1.3));
        dynamoDBMapper.save(new Power(user1.getId(), site2.getSiteId(), base.plusMinutes(10), 1.2));
        dynamoDBMapper.save(new Power(user1.getId(), site2.getSiteId(), base.plusMinutes(13), 1.1));
        dynamoDBMapper.save(new Power(user1.getId(), site2.getSiteId(), base.plusMinutes(25), 0.9));

        base = LocalDateTime.parse("2021-08-05T10:12:00");
        dynamoDBMapper.save(new Power(user2.getId(), site3.getSiteId(), base.plusMinutes(0), 7.3));
        dynamoDBMapper.save(new Power(user2.getId(), site3.getSiteId(), base.plusMinutes(10), 4.3));

        base = LocalDateTime.parse("2014-02-28T13:08:10");
        dynamoDBMapper.save(new Power(user3.getId(), site4.getSiteId(), base.plusMinutes(0), 4.3));
        dynamoDBMapper.save(new Power(user3.getId(), site4.getSiteId(), base.plusDays(10), 8.3));
        dynamoDBMapper.save(new Power(user3.getId(), site4.getSiteId(), base.plusDays(8), 0.1));
    }
}
