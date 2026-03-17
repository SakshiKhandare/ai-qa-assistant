package com.sakshi.ai_qa_assistant.entity;

import lombok.Data;

import java.util.Map;

@Data
public class ApiTestRequest {

    private String endpoint;
    private String method;

    private Map<String, Object> schema;

}