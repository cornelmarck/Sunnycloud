package com.cornelmarck.sunnycloud.service;

import com.cornelmarck.sunnycloud.dto.SolaredgePowerDto;
import com.cornelmarck.sunnycloud.dto.SolaredgeSiteDto;
import com.cornelmarck.sunnycloud.exception.SolaredgeApiException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Type;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SolaredgeSiteService {
    private final RestTemplate restTemplate;
    private final String endpoint = "https://monitoringapi.solaredge.com/sites/list.json?api_key=";

    public List<SolaredgeSiteDto> getAllSites(String apiKey) {
        //Todo: Error handling https://www.baeldung.com/spring-rest-template-error-handling
        try {
            ResponseEntity<JsonNode> entity = restTemplate.getForEntity(endpoint + apiKey, JsonNode.class);
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode siteListNode = entity.getBody().get("sites").get("site");
            ObjectReader objectReader = objectMapper.readerFor(new TypeReference<List<SolaredgeSiteDto>>(){});
            List<SolaredgeSiteDto> siteDtoList = objectReader.readValue(siteListNode);
            return siteDtoList;
        }
        catch (Exception e) {
            throw new SolaredgeApiException(e.getMessage());
        }

    }
}
