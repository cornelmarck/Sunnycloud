package com.cornelmarck.sunnycloud;

import com.cornelmarck.sunnycloud.model.Power;
import com.cornelmarck.sunnycloud.repository.PowerRepository;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.UUID;

@SpringBootTest(classes = SunnycloudApplication.class)
@TestPropertySource(properties = {"amazon.dynamodb.endpoint=http://localhost:8000/", "amazon.aws.accesskey=test1", "amazon.aws.secretkey=test2" })
public class PowerIntegrationTest {
    @Autowired
    PowerRepository powerRepository;

    @Test
    public void insertAndRetrieveSingleItem() {
        Power one = new Power("example@hello.com",
                UUID.fromString("2136c30e-ba8d-4fd3-a711-90964a1954b6"),
                LocalDateTime.now());
        one.setPower(4.12);

        powerRepository.save(one);
    }


}
