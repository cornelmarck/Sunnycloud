package com.cornelmarck.sunnycloud;


import com.cornelmarck.sunnycloud.model.User;
import com.cornelmarck.sunnycloud.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;
import java.util.UUID;

@SpringBootTest(classes = SunnycloudApplication.class)
@TestPropertySource(properties = {"amazon.dynamodb.endpoint=http://localhost:8000/", "amazon.aws.accesskey=test1", "amazon.aws.secretkey=test2" })
public class UserRepositoryTest {
    @Autowired
    UserRepository userRepository;

    @Test
    public void insertSingleItem() {
        User one = new User();
        one.setEmailAddress("asdf@adfs.com");
        one.setMobileNumber("+23423423324");
        userRepository.save(one);
    }

    @Test
    public void retrieveSingleItem() {
        User.PrimaryKey primaryKey = new User.PrimaryKey();
        primaryKey.setId(UUID.fromString("2e9e6a4e-9287-45f9-8f93-5e2b2d373240"));

        Optional<User> found = userRepository.findById(primaryKey);
        Assertions.assertEquals("asdf", found.get().getEmailAddress());
    }


}
