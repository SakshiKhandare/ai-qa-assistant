package com.sakshi.ai_qa_assistant.ai;

public class PromptTemplates {

    public static final String API_TEST_PROMPT = """
            You are a senior QA automation engineer with expertise in REST API testing.
            
            Generate comprehensive API test cases for the following endpoint.
            
            Endpoint: %s
            Method: %s
            Request Body Schema: %s
            
            Generate at least 8 test cases covering ALL of the following categories:
            
            1. Positive scenarios — valid input, expected success response
            2. Negative scenarios — invalid input, missing fields, wrong data types
            3. Boundary scenarios — empty strings, null values, max/min lengths, special characters
            4 . HTTP method specific:
               - GET: test with valid id, invalid id, non-existent resource
               - POST: test with duplicate data, missing required fields
               - PUT/PATCH: test partial updates, updating non-existent resource
               - DELETE: test deleting non-existent resource, unauthorized delete
            
            STRICT RULES:
            1. Return ONLY a single JSON array — no extra text, no markdown, no code blocks
            2. Do NOT split into multiple arrays
            3. Do NOT add explanations outside the JSON
            4. Each test case must cover a UNIQUE validation condition
            5. statusCode must be a realistic HTTP code (200, 201, 400, 401, 403, 404, 409, 422, 500)
            6. responseBody must contain realistic expected fields, not just {}
            7. input must contain realistic values matching the schema types
            
            Return JSON in this exact format:
            [
              {
                "testName": "Valid User Registration",
                "description": "Positive: register with all valid fields",
                "input": {
                  "email": "john@example.com",
                  "password": "SecurePass@123"
                },
                "expectedResult": {
                  "statusCode": 201,
                  "responseBody": {
                    "message": "User registered successfully",
                    "userId": 1
                  }
                }
              }
            ]
            
            Return only the JSON array.
            """;

    public static final String TEST_DATA_PROMPT = """
            You are a QA engineer specializing in test data generation.
            
            Given the following schema, generate %d test data objects.
            
            Schema:
            %s
            
            Generate a MIX of the following data types:
            - Valid realistic data (at least 40%%)
            - Boundary values — empty strings, zero, max length values (at least 20%%)
            - Invalid data — wrong types, negative numbers, malformed emails (at least 20%%)
            - Edge cases — special characters, very long strings, null values (at least 20%%)
            
            STRICT RULES:
            1. Return ONLY a JSON array — no markdown, no explanation
            2. Maintain correct data types as defined in the schema
            3. Use realistic values — real-looking names, proper email formats for valid cases
            4. Each object must be meaningfully different from others
            5. Invalid data objects should have values that would realistically fail validation
            
            Return only the JSON array.
            """;
}