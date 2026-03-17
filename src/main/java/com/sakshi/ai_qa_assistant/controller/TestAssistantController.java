package com.sakshi.ai_qa_assistant.controller;

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
                                   CodeGeneratorService codeGeneratorService, TestDataService testDataService) {
        this.testCaseService = testCaseService;
        this.codeGeneratorService = codeGeneratorService;
        this.testDataService = testDataService;
    }

    @PostMapping("/api-tests")
    public List<ApiTestCase> generateApiTests(@RequestBody ApiTestRequest request) {
        return testCaseService.generateApiTests(request);
    }

    @PostMapping("/restassured-tests")
    public String generateRestAssuredTests(@RequestBody ApiTestRequest request) {

        List<ApiTestCase> testCases =
                testCaseService.generateApiTests(request);

        return codeGeneratorService.generateRestAssuredTests(
                request.getEndpoint(),
                request.getMethod(),
                testCases
        );
    }

    @PostMapping("/test-data")
    public List<Map<String,Object>> generateTestData(
            @RequestBody TestDataRequest request) {

        return testDataService.generateTestData(request);
    }
}