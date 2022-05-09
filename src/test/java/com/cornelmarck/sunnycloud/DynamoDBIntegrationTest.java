package com.cornelmarck.sunnycloud;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.cornelmarck.sunnycloud.model.User;
import com.cornelmarck.sunnycloud.repository.UserRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(classes = SunnycloudApplication.class)
@TestPropertySource(properties = {"amazon.dynamodb.endpoint=http://localhost:8000/", "amazon.aws.accesskey=test1", "amazon.aws.secretkey=test2" })
public class DynamoDBIntegrationTest {
    @Autowired
    private AmazonDynamoDB amazonDynamoDB;

    @Autowired
    @Qualifier("DynamoSDK")
    private DynamoDBMapper dynamoDBMapper;

    @Test
    public void mainTableExists() {
        Assertions.assertEquals("Main", amazonDynamoDB.listTables().getTableNames().get(0));
    }

    @Test
    public void insertUser() {
        User user = new User("asdf", "asdf");
        dynamoDBMapper.save(user);
    }
}
