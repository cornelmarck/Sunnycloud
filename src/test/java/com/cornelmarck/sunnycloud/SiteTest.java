package com.cornelmarck.sunnycloud;

import com.cornelmarck.sunnycloud.dto.Site;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.cfg.ConstructorDetector;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

class SiteTest {
    File siteJson;
    File siteListJson;

    @BeforeEach
    void init() {
        siteJson = new File(SiteTest.class.getResource("/site.json").getFile());
        siteListJson = new File(SiteTest.class.getResource("/site-list.json").getFile());
    }

    @Test
    void singleSite() throws IOException {
        ObjectMapper mapper = JsonMapper.builder()
                .addModule(new ParameterNamesModule())
                .constructorDetector(ConstructorDetector.USE_PROPERTIES_BASED)
                .build();

        Site site = mapper.readValue(siteJson, Site.class);
        Assertions.assertEquals("Amsterdam", site.location.city);
        Assertions.assertEquals(12341234, site.id);
    }

    @Test
    void siteList() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode tree = mapper.readTree(siteListJson).get("sites").get("site");

        ObjectMapper siteMapper = JsonMapper.builder()
                .addModule(new ParameterNamesModule())
                .constructorDetector(ConstructorDetector.USE_PROPERTIES_BASED)
                .build();

        List<Site> siteList = Arrays.asList(siteMapper.treeToValue(tree, Site[].class));
        Site site = siteList.get(0);

        Assertions.assertEquals("Amsterdam", site.location.city);
        Assertions.assertEquals(12341234, site.id);
    }

}