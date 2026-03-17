package com.sakshi.ai_qa_assistant.ai;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class AiService {

    @Value("${nvidia.api.key}")
    private String apiKey;

    private static final String NVIDIA_API_URL =
            "https://integrate.api.nvidia.com/v1/chat/completions";

    private final RestTemplate restTemplate = new RestTemplate();

    public String callAi(String prompt) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        Map<String, Object> requestBody = Map.of(
                "model", "meta/llama3-8b-instruct",
                "messages", new Object[]{
                        Map.of(
                                "role", "user",
                                "content", prompt
                        )
                },
                "max_tokens", 1000
        );

        HttpEntity<Map<String, Object>> request =
                new HttpEntity<>(requestBody, headers);

        Map<String, Object> responseMap =
                restTemplate.exchange(
                        NVIDIA_API_URL,
                        HttpMethod.POST,
                        request,
                        Map.class
                ).getBody();

        Map<String, Object> choice =
                (Map<String, Object>) ((java.util.List<?>) responseMap.get("choices")).get(0);

        Map<String, Object> message =
                (Map<String, Object>) choice.get("message");

        return (String) message.get("content");
    }
}