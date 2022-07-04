package com.cornelmarck.sunnycloud;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.cornelmarck.sunnycloud.model.Power;
import com.cornelmarck.sunnycloud.model.Site;
import com.cornelmarck.sunnycloud.model.User;
import com.cornelmarck.sunnycloud.repository.PowerRepository;
import com.cornelmarck.sunnycloud.repository.SiteRepository;
import com.cornelmarck.sunnycloud.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Disabled
@SpringBootTest(classes = SunnycloudApplication.class)
public class DynamoDbIT {
    @Autowired
    private AmazonDynamoDB amazonDynamoDB;
    @Autowired
    private DynamoDBMapper dynamoDBMapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SiteRepository siteRepository;
    @Autowired
    private PowerRepository powerRepository;

    @BeforeEach
    public void init() throws Exception {
        Utils utils = new Utils(amazonDynamoDB, dynamoDBMapper);
        utils.deleteTables();
        utils.createMainTable();
        utils.createPowerTable();
    }

    @Test
    public void mainTableExists() {
        Assertions.assertEquals("Main", amazonDynamoDB.listTables().getTableNames().get(0));
    }

    @Test
    public void insertAndRetrieveUser() {
        User user = new User();
        user.setEmailAddress("john@smith.com");
        user.setName("John Smith");
        user.setMobilePhoneNumber(("+1 2345 890123"));
        userRepository.save(user);

        Optional<User> found = userRepository.findByEmailAddress("john@smith.com");
        Assertions.assertTrue(found.isPresent());
        Assertions.assertEquals("John Smith", found.get().getName());
    }

    @Test
    public void insertAndRetrieveSite() {
        Site one = new Site();
        one.setName("CloudSite");
        one.setTimeZone("UTC");
        one.setPeakPower(4.4);
        siteRepository.save(one);

        Optional<Site> found = siteRepository.findById(one.getId());
        Assertions.assertTrue(found.isPresent());
        Assertions.assertEquals(4.4, found.get().getPeakPower());
    }

    @Test
    public void insertAndRetrieveSitesByOwnerId() {
        Site one = new Site();
        one.setName("CloudSite");
        one.setTimeZone("UTC");
        one.setOwnerId("john@smith.com");
        siteRepository.save(one);

        Site two = new Site();
        two.setName("Otherside");
        two.setTimeZone("Europe/Berlin");
        two.setAzimuthAngle(42.3);
        two.setOwnerId("john@smith.com");
        siteRepository.save(two);

        List<Site> siteList = siteRepository.findByOwner("john@smith.com");
        Assertions.assertEquals(2, siteList.size());
    }

    @Test
    public void insertAndRetrievePower() {
        Site one = new Site();
        one.setName("CloudSite");
        one.setTimeZone("America/Los_Angeles");
        one.setOwnerId("john@smith.com");
        siteRepository.save(one);

        String id = one.getId();
        powerRepository.save(new Power(id, Instant.parse("2021-03-21T23:15:00Z"), 3401.1));
        powerRepository.save(new Power(id, Instant.parse("2021-03-21T23:30:00Z"), 4331.4));
        powerRepository.save(new Power(id, Instant.parse("2021-03-21T23:45:00Z"), 4221));
        powerRepository.save(new Power(id, Instant.parse("2021-03-22T00:00:00Z"), 5321));
        powerRepository.save(new Power(id, Instant.parse("2021-03-22T00:15:00Z"), 2789));
        powerRepository.save(new Power(id, Instant.parse("2021-03-22T00:30:00Z"), 4999.4));

        List<Power> powerList = powerRepository.findAllBySiteIdAndTimestampBetween(id, Instant.parse("2021-03-21T23:30:00Z"),
                Instant.parse("2021-03-22T00:15:00Z"));
        Assertions.assertEquals(3, powerList.size());
    }

}
