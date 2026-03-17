package com.sakshi.ai_qa_assistant.entity;

import lombok.Data;

import java.util.Map;

@Data
public class TestDataRequest {

    private Map<String, Object> schema;

    public Map<String, Object> getSchema() {
        return schema;
    }

    public void setSchema(Map<String, Object> schema) {
        this.schema = schema;
    }
}