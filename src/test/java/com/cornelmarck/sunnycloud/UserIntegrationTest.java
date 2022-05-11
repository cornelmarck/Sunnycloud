package com.cornelmarck.sunnycloud;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.cornelmarck.sunnycloud.model.User;
import com.cornelmarck.sunnycloud.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.Optional;

@SpringBootTest(classes = SunnycloudApplication.class)
@TestPropertySource(properties = {"amazon.dynamodb.endpoint=http://localhost:8000/", "amazon.aws.accesskey=test1", "amazon.aws.secretkey=test2" })
public class UserIntegrationTest {
    @Autowired
    AmazonDynamoDB amazonDynamoDB;
    @Autowired
    DynamoDBMapper dynamoDBMapper;
    @Autowired
    UserRepository userRepository;

    @BeforeEach
    public void init() throws Exception {
        Utils utils = new Utils(amazonDynamoDB, dynamoDBMapper);
        utils.deleteMainTable();
        utils.createMainTable();
        utils.populateMainTable();
    }

    @Test
    public void findExistingUser() {
        Optional<User> user = userRepository.findByIdAndSortKeyEquals("ex2@hello.com", "UserDetails");
        Assertions.assertTrue(user.isPresent());
        Assertions.assertEquals("2345 6789", user.get().getMobileNumber());
    }

    @Test
    public void findAllUsers() {
        List<User> allUsers = userRepository.findAllBySortKeyEquals("UserDetails");
        Assertions.assertEquals(3, allUsers.size());
    }
}
