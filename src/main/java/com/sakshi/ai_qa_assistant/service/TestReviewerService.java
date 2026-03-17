package com.sakshi.ai_qa_assistant.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sakshi.ai_qa_assistant.ai.AiService;
import com.sakshi.ai_qa_assistant.ai.PromptTemplates;
import com.sakshi.ai_qa_assistant.entity.TestReviewRequest;
import com.sakshi.ai_qa_assistant.entity.TestReviewResult;
import org.springframework.stereotype.Service;

@Service
public class TestReviewerService {

    private final AiService aiService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public TestReviewerService(AiService aiService) {
        this.aiService = aiService;
    }

    public TestReviewResult reviewTests(TestReviewRequest request) {

        if (request.getTestCode() == null || request.getTestCode().isBlank()) {
            throw new IllegalArgumentException("Test code cannot be empty");
        }

        try {
            // STEP 1 - Build the prompt
            String prompt = String.format(
                    PromptTemplates.TEST_REVIEW_PROMPT,
                    request.getFramework() != null ? request.getFramework() : "unknown",
                    request.getEndpoint() != null ? request.getEndpoint() : "unknown",
                    request.getTestCode()
            );

            // STEP 2 - Call AI
            String aiResponse = aiService.callAi(prompt);

            System.out.println("Raw AI Response:");
            System.out.println(aiResponse);

            // STEP 3 - Extract JSON object from response
            int start = aiResponse.indexOf("{");
            int end = aiResponse.lastIndexOf("}") + 1;

            if (start < 0 || end <= start) {
                throw new RuntimeException("AI returned invalid JSON structure");
            }

            String json = aiResponse.substring(start, end);

            System.out.println("Extracted JSON:");
            System.out.println(json);

            // STEP 4 - Convert JSON to Java object
            return objectMapper.readValue(json, TestReviewResult.class);

        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to review test file: " + e.getMessage(), e);
        }
    }
}