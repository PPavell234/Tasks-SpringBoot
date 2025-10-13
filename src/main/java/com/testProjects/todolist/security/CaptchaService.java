package com.testProjects.todolist.security;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CaptchaService {
    private static final String GOOGLE_VERIFY_URL = "https://www.google.com/recaptcha/api/siteverify";
    private static final Logger logger = LoggerFactory.getLogger(CaptchaService.class);

    @Value("${google.recaptcha.secret}")
    private String recaptchaSecret;

    private final RestTemplate restTemplate;

    // Внедряем RestTemplate через конструктор для лучшей тестируемости
    public CaptchaService() {
        this.restTemplate = new RestTemplate();
        // Добавляем таймауты для избежания зависаний
        this.restTemplate.setRequestFactory(new SimpleClientHttpRequestFactory());
        ((SimpleClientHttpRequestFactory) this.restTemplate.getRequestFactory()).setConnectTimeout(5000);
        ((SimpleClientHttpRequestFactory) this.restTemplate.getRequestFactory()).setReadTimeout(5000);
    }

    public boolean verifyCaptcha(String recaptchaResponse) {
        // Проверка на null и пустую строку
        if (recaptchaResponse == null || recaptchaResponse.trim().isEmpty()) {
            logger.warn("reCAPTCHA response is empty or null");
            return false;
        }

        try {
            MultiValueMap<String, String> requestData = new LinkedMultiValueMap<>();
            requestData.add("secret", recaptchaSecret);
            requestData.add("response", recaptchaResponse);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            HttpEntity<MultiValueMap<String, String>> requestEntity =
                    new HttpEntity<>(requestData, headers);

            // Используем более специфичный тип для ответа
            ResponseEntity<ReCaptchaResponse> googleResponse =
                    restTemplate.postForEntity(GOOGLE_VERIFY_URL, requestEntity, ReCaptchaResponse.class);

            if (googleResponse.getBody() == null) {
                logger.error("Empty response from reCAPTCHA verification service");
                return false;
            }

            boolean isSuccess = googleResponse.getBody().isSuccess();

            // Логируем для отладки
            if (!isSuccess) {
                logger.warn("reCAPTCHA verification failed. Errors: {}",
                        googleResponse.getBody().getErrorCodes());
            }

            return isSuccess;

        } catch (Exception e) {
            logger.error("Error during reCAPTCHA verification: {}", e.getMessage());
            // В случае ошибки сети и т.д. - можно разрешить доступ или заблокировать
            // Обычно лучше заблокировать при ошибке верификации
            return false;
        }
    }

    // Вспомогательный класс для парсинга ответа
    public static class ReCaptchaResponse {
        private boolean success;
        private String challenge_ts;
        private String hostname;
        private List<String> errorCodes;

        // геттеры и сеттеры
        public boolean isSuccess() { return success; }
        public void setSuccess(boolean success) { this.success = success; }
        public List<String> getErrorCodes() { return errorCodes; }
        public void setErrorCodes(List<String> errorCodes) { this.errorCodes = errorCodes; }
    }
}