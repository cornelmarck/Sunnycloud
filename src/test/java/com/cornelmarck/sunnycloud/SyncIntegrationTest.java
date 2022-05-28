//package com.cornelmarck.sunnycloud;
//
//import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
//import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
//import com.cornelmarck.sunnycloud.model.Location;
//import com.cornelmarck.sunnycloud.model.Site;
//import com.cornelmarck.sunnycloud.model.User;
//import com.cornelmarck.sunnycloud.repository.SyncGatewayRepository;
//import com.cornelmarck.sunnycloud.service.AbstractSyncGateway;
//import com.cornelmarck.sunnycloud.service.SmaSyncGateway;
//import com.cornelmarck.sunnycloud.service.SolaredgeSyncService;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.TestPropertySource;
//
//import java.util.Optional;
//
//@SpringBootTest(classes = SunnycloudApplication.class)
//@TestPropertySource(locations = "classpath:test.properties")
//public class SyncIntegrationTest {
//    @Autowired
//    private DynamoDBMapper dynamoDBMapper;
//    @Autowired
//    private AmazonDynamoDB amazonDynamoDB;
//    @Autowired
//    private SyncGatewayRepository repository;
//
//    @BeforeEach
//    public void init() throws Exception {
//        Utils utils = new Utils(amazonDynamoDB, dynamoDBMapper);
//        utils.deleteMainTable();
//        utils.createMainTable();
//    }
//
//    @Test
//    public void insertOne() {
//        SolaredgeSyncService gateway = new SolaredgeSyncService();
//        gateway.setApiKey("asdf");
//        gateway.setSiteId("2");
//        repository.save((gateway));
//    }
//
//    @Test
//    public void insertAndRetrieveOne() {
//        SolaredgeSyncService gateway = new SolaredgeSyncService();
//        gateway.setApiKey("asdf");
//        gateway.setSiteId("2");
//        repository.save((gateway));
//
//        Optional<AbstractSyncGateway> found = repository.getBySiteId("2");
//        Assertions.assertTrue(found.isPresent());
//        Assertions.assertEquals(SolaredgeSyncService.class, found.get().getClass());
//    }
//
//    @Test
//    public void insertBoth() {
//        SolaredgeSyncService solaredge = new SolaredgeSyncService();
//        solaredge.setSiteId("2");
//        solaredge.setApiKey("fffff");
//        repository.save((solaredge));
//
//        SmaSyncGateway sma = new SmaSyncGateway();
//        sma.setSiteId("1");
//        sma.setApiKey("asdf");
//        repository.save(sma);
//
//
//    }
//
//
//    @Test
//    public void run() {
//
//    }
//
//    public void populateSyncTest() {
//        User user1 = new User();
//        user1.setEmailAddress("ex1@hello.com");
//        user1.setName("Harry Styles");
//        user1.setMobilePhoneNumber("1234 5678");
//        dynamoDBMapper.save(user1);
//
//        Site site = new Site();
//        site.setOwnerId(user1.getEmailAddress());
//        site.setName("Back garden PV");
//        site.setPeakPower(3.67);
//        site.setTiltAngle(21.4);
//        site.setAzimuthAngle(0.12);
//        site.setLocation(new Location());
//        site.getLocation().setLatitude(51.5072);
//        site.getLocation().setLongitude(-0.12755);
//        site.getLocation().setCity("London");
//        site.getLocation().setAddress1("34 Windsor Drive");
//        site.getLocation().setZipCode("W3 5CX");
//        site.getLocation().setCountry("United Kingdom");
//        site.getLocation().setCountryCode("UK");
//        site.getLocation().setTimeZone("UTC");
//        dynamoDBMapper.save(site);
//    }
//
//}
