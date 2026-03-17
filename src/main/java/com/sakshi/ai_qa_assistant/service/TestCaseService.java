package com.sakshi.ai_qa_assistant.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sakshi.ai_qa_assistant.ai.AiService;
import com.sakshi.ai_qa_assistant.ai.PromptTemplates;
import com.sakshi.ai_qa_assistant.entity.ApiTestCase;
import com.sakshi.ai_qa_assistant.entity.ApiTestRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TestCaseService {

    private final AiService aiService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public TestCaseService(AiService aiService) {
        this.aiService = aiService;
    }

    public List<ApiTestCase> generateApiTests(ApiTestRequest request) {

        int maxRetries = 2;
        int attempt = 0;

        while (attempt <= maxRetries) {

            try {

                // STEP 1 — Convert schema to proper JSON
                String schemaJson = objectMapper
                        .writerWithDefaultPrettyPrinter()
                        .writeValueAsString(request.getSchema());

                // STEP 2 — Build prompt
                String prompt = String.format(
                        PromptTemplates.API_TEST_PROMPT,
                        request.getEndpoint(),
                        request.getMethod(),
                        schemaJson
                );

                // STEP 3 — Call AI
                String aiResponse = aiService.callAi(prompt);

                System.out.println("Raw AI Response:");
                System.out.println(aiResponse);

                // STEP 4 — Extract JSON
                int start = aiResponse.indexOf("[");
                int end = aiResponse.lastIndexOf("]") + 1;

                if (start < 0 || end <= start) {
                    throw new RuntimeException("Invalid JSON structure returned by AI");
                }

                String jsonArray = aiResponse.substring(start, end);

                System.out.println("Extracted JSON:");
                System.out.println(jsonArray);

                // STEP 5 — Convert JSON → Java objects
                List<ApiTestCase> testCases =
                        objectMapper.readValue(jsonArray, new TypeReference<List<ApiTestCase>>() {});

                return testCases;

            } catch (Exception e) {

                System.out.println("AI parsing failed. Attempt: " + attempt);

                if (attempt == maxRetries) {
                    throw new RuntimeException("AI failed to generate valid test cases after retries");
                }

                attempt++;
            }
        }

        throw new RuntimeException("Unexpected error while generating test cases");
    }
}