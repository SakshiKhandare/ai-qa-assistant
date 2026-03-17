package com.sakshi.ai_qa_assistant.ai;

public class PromptTemplates {

    public static final String API_TEST_PROMPT = """
            You are a senior QA automation engineer.
            
            Generate comprehensive API test cases for the following endpoint.
            
            Endpoint: %s
            Method: %s
            
            Request Body Schema:
            %s
            
            Generate test cases covering:
            - Positive scenarios
            - Negative scenarios
            - Boundary scenarios
            
            IMPORTANT RULES:
            
            1. Return ONLY ONE JSON ARRAY.
            2. Do NOT create separate arrays for categories.
            3. Do NOT include explanations or markdown.
            4. Do NOT include headings like "Positive Test Cases".
            5. Include the category in the description if needed.
            6. Avoid duplicate scenarios.
            7. Ensure each test case covers a unique validation condition.
            
            Expected JSON format:
            
            [
              {
                "testName": "Valid Login",
                "description": "Positive case: login with valid credentials",
                "input": {},
                "expectedResult": {
                  "statusCode": 200,
                  "responseBody": {}
                }
              }
            ]
            
            Return only the JSON array.
            """;

    public static final String TEST_DATA_PROMPT = """
            You are a QA engineer generating test data.
            
            Given the following schema, generate 5 realistic test data objects.
            
            Schema:
            %s
            
            Rules:
            - Use realistic values
            - Maintain correct data types
            - Include boundary values where applicable
            
            Return output strictly as JSON array.
            """;
}