package com.sakshi.ai_qa_assistant.entity;

import lombok.Data;
import java.util.List;

@Data
public class TestReviewResult {

    private int totalTestsFound;
    private String coverageScore;
    private String overallFeedback;
    private List<String> missingScenarios;
    private List<String> suggestions;
    private List<String> goodPracticesFound;
}