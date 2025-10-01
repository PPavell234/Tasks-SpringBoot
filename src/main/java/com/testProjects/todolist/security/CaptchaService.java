package com.testProjects.todolist.security;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
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
        Map<String, String> body = new HashMap<>();
        body.put("secret", recaptchaSecret);
        body.put("response", response);

        ResponseEntity<Map> googleResponse = restTemplate.postForEntity(
                GOOGLE_VERIFY_URL, body, Map.class);

        if (googleResponse.getBody() == null) return false;
        return (Boolean) googleResponse.getBody().get("success");
    }
}