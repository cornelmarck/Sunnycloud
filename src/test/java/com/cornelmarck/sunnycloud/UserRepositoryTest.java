package com.cornelmarck.sunnycloud;


import com.cornelmarck.sunnycloud.model.User;
import com.cornelmarck.sunnycloud.model.UserPrimaryKey;
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
    public void insertAndRetrieveSingleItem() {
        User one = new User("example@hello.com");
        one.setMobileNumber("+44 8942 244103");
        userRepository.save(one);

        UserPrimaryKey key = new UserPrimaryKey("example@hello.com");
        Optional<User> found = userRepository.findById(key);

        Assertions.assertTrue(found.isPresent());
        Assertions.assertEquals("+44 8942 244103", found.get().getMobileNumber());
    }

}
