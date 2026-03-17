package com.sakshi.ai_qa_assistant.entity;

import lombok.Data;

@Data
public class TestReviewRequest {

    private String testCode;
    private String endpoint;
    private String framework;
}