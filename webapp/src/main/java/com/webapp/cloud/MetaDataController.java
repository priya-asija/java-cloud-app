package com.webapp.cloud;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

@RestController
public class MetaDataController {

    private final String METADATA_BASE = "http://169.254.169.254/latest/meta-data/placement/";

    @GetMapping("/metadata")
    public Map<String,String> getMetaData() throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        String az = getMetaDataValue(client, "availability-zone");
        Map<String, String> result = new HashMap<>();
        if (az != null && az.length() > 1) {
            String region = az.substring(0, az.length() - 1);
            result.put("availabilityZone", az);
            result.put("region", region);
        } else {
            result.put("availabilityZone", "unknown");
            result.put("region", "unknown");
        }
    return result;
    }


    private String getMetaDataValue(HttpClient client, String path) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(METADATA_BASE + path))
                .GET()
                .build();
        return client.send(request, HttpResponse.BodyHandlers.ofString()).body();
    }

}
