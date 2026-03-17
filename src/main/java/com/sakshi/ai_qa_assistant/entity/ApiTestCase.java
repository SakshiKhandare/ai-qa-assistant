package com.sakshi.ai_qa_assistant.entity;

import lombok.Data;
import java.util.Map;

@Data
public class ApiTestCase {

    private String testName;

    private String description;

    private Map<String, Object> input;

    private Map<String, Object> expectedResult;

}