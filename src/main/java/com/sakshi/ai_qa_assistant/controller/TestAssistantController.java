package com.sakshi.ai_qa_assistant.controller;

import com.sakshi.ai_qa_assistant.entity.ApiResponse;
import com.sakshi.ai_qa_assistant.entity.ApiTestCase;
import com.sakshi.ai_qa_assistant.entity.ApiTestRequest;
import com.sakshi.ai_qa_assistant.entity.TestDataRequest;
import com.sakshi.ai_qa_assistant.service.CodeGeneratorService;
import com.sakshi.ai_qa_assistant.service.TestCaseService;
import com.sakshi.ai_qa_assistant.service.TestDataService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/generate")
public class TestAssistantController {

    private final TestCaseService testCaseService;
    private final CodeGeneratorService codeGeneratorService;
    private final TestDataService testDataService;

    public TestAssistantController(TestCaseService testCaseService,
                                   CodeGeneratorService codeGeneratorService,
                                   TestDataService testDataService) {
        this.testCaseService = testCaseService;
        this.codeGeneratorService = codeGeneratorService;
        this.testDataService = testDataService;
    }

    @PostMapping("/api-tests")
    public ApiResponse<List<ApiTestCase>> generateApiTests(
            @RequestBody ApiTestRequest request) {

        List<ApiTestCase> testCases = testCaseService.generateApiTests(request);
        return ApiResponse.success("Test cases generated successfully", testCases);
    }

    @PostMapping("/restassured-tests")
    public ApiResponse<String> generateRestAssuredTests(
            @RequestBody ApiTestRequest request) {

        List<ApiTestCase> testCases = testCaseService.generateApiTests(request);
        String result = codeGeneratorService.generateRestAssuredTests(
                request.getEndpoint(),
                request.getMethod(),
                testCases
        );
        return ApiResponse.success("RestAssured test file generated successfully", result);
    }

    @PostMapping("/test-data")
    public ApiResponse<List<Map<String, Object>>> generateTestData(
            @RequestBody TestDataRequest request) {

        List<Map<String, Object>> data = testDataService.generateTestData(request);
        return ApiResponse.success("Test data generated successfully", data);
    }
}