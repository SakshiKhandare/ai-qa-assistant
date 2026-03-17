package com.sakshi.ai_qa_assistant.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sakshi.ai_qa_assistant.ai.AiService;
import com.sakshi.ai_qa_assistant.ai.PromptTemplates;
import com.sakshi.ai_qa_assistant.entity.TestDataRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TestDataService {

    private final AiService aiService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public TestDataService(AiService aiService) {
        this.aiService = aiService;
    }

    public List<Map<String,Object>> generateTestData(TestDataRequest request) {

        try {

            String schemaJson = objectMapper
                    .writerWithDefaultPrettyPrinter()
                    .writeValueAsString(request.getSchema());

            String prompt = String.format(
                    PromptTemplates.TEST_DATA_PROMPT,
                    5,
                    schemaJson
            );

            String aiResponse = aiService.callAi(prompt);

            System.out.println("Raw AI Response:");
            System.out.println(aiResponse);

            int start = aiResponse.indexOf("[");
            int end = aiResponse.lastIndexOf("]") + 1;

            String json = aiResponse.substring(start,end);

            return objectMapper.readValue(
                    json,
                    new TypeReference<List<Map<String,Object>>>() {}
            );

        } catch (Exception e) {
            throw new RuntimeException("Failed to generate test data", e);
        }
    }
}