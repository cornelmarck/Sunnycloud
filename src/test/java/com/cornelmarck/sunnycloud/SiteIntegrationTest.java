package com.cornelmarck.sunnycloud;

import com.cornelmarck.sunnycloud.SunnycloudApplication;
import com.cornelmarck.sunnycloud.model.Site;
import com.cornelmarck.sunnycloud.model.SitePrimaryKey;
import com.cornelmarck.sunnycloud.repository.SiteRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.Optional;

@SpringBootTest(classes = SunnycloudApplication.class)
@TestPropertySource(properties = {"amazon.dynamodb.endpoint=http://localhost:8000/", "amazon.aws.accesskey=test1", "amazon.aws.secretkey=test2" })
public class SiteIntegrationTest {
    @Autowired
    SiteRepository siteRepository;

    @Test
    public void insertAndRetrieveSingleItem() {
        Site one = new Site("example@hello.com");
        one.setName("Back garden installation");
        one.setCity("London");
        one.setAddress("24 Park Street");
        one.setZip("W3 7CX");
        one.setTimeZone(0);
        one.setCountry("United Kingdom");
        one.setCountryCode("UK");
        siteRepository.save(one);

        SitePrimaryKey key = new SitePrimaryKey("example@hello.com", one.getSiteId());
        Optional<Site> found = siteRepository.findById(key);
        Assertions.assertTrue(found.isPresent());
        Assertions.assertEquals(one, found.get());
    }

    @Test
    public void getAllSitesByUser() {
        List<Site> sites = siteRepository.findSitesByUserIdAndSortKeyStartsWith("example@hello.com", "SiteDetails");
        Assertions.assertFalse(sites.isEmpty());
    }

    @Test
    public void getAllSitesByInvalidUser() {
        List<Site> sites = siteRepository.findSitesByUserIdAndSortKeyStartsWith("invalid", "SiteDetails");
        Assertions.assertTrue(sites.isEmpty());
    }
}
