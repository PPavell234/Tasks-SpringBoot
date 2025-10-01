package com.testProjects.todolist.security;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class CaptchaService {
    private static final String GOOGLE_VERIFY_URL = "https://www.google.com/recaptcha/api/siteverify";

    @Value("${google.recaptcha.secret}")
    private String recaptchaSecret;

    private final RestTemplate restTemplate = new RestTemplate();

    public boolean verifyCaptcha(String response) {
        if (response == null || response.isEmpty()) {
            return false;
        }

        MultiValueMap<String, String> requestData = new LinkedMultiValueMap<>();
        requestData.add("secret", recaptchaSecret);
        requestData.add("response", response);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> requestEntity =
                new HttpEntity<>(requestData, headers);

        ResponseEntity<Map> googleResponse =
                restTemplate.postForEntity(GOOGLE_VERIFY_URL, requestEntity, Map.class);

        if (googleResponse.getBody() == null) {
            return false;
        }

        Object success = googleResponse.getBody().get("success");
        return success != null && (Boolean) success;
    }
}