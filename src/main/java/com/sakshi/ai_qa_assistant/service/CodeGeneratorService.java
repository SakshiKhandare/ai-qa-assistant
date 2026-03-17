package com.sakshi.ai_qa_assistant.service;

import com.sakshi.ai_qa_assistant.entity.ApiTestCase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.util.List;
import java.util.Map;

@Service
public class CodeGeneratorService {

    @Value("${test.output.path}")
    private String testOutputPath;

    public String generateRestAssuredTests(String endpoint,
                                           String method,
                                           List<ApiTestCase> testCases) {

        String className = formatClassName(endpoint);

        StringBuilder code = new StringBuilder();

        code.append("package generated;\n\n");
        code.append("import static io.restassured.RestAssured.*;\n");
        code.append("import io.restassured.RestAssured;\n");
        code.append("import io.restassured.http.ContentType;\n");
        code.append("import org.junit.jupiter.api.BeforeAll;\n");
        code.append("import org.junit.jupiter.api.Test;\n");
        code.append("import java.util.Map;\n\n");

        code.append("public class ").append(className).append(" {\n\n");

        code.append("    @BeforeAll\n");
        code.append("    public static void setup() {\n");
        code.append("        RestAssured.baseURI = \"http://localhost:8080\";\n");
        code.append("    }\n\n");

        for (ApiTestCase test : testCases) {

            code.append("    @Test\n");
            code.append("    public void ")
                    .append(formatMethodName(test.getTestName()))
                    .append("() {\n\n");

            if (!test.getInput().isEmpty()) {
                code.append(generateMapCode("requestBody", test.getInput(), 2));
            }

            int statusCode =
                    (Integer) test.getExpectedResult().getOrDefault("statusCode", 200);

            code.append("\n");

            code.append("        given()\n")
                    .append("            .contentType(ContentType.JSON)\n");

            if (!test.getInput().isEmpty()) {
                code.append("            .body(requestBody)\n");
            }

            code.append("        .when()\n")
                    .append("            .")
                    .append(method.toLowerCase())
                    .append("(\"")
                    .append(endpoint)
                    .append("\")\n")
                    .append("        .then()\n")
                    .append("            .statusCode(")
                    .append(statusCode)
                    .append(");\n\n");

            code.append("    }\n\n");
        }

        code.append("}");

        saveToFile(className, code.toString());

        return "RestAssured test file generated: " + className + ".java";
    }

    private String generateMapCode(String varName,
                                   Map<String, Object> map,
                                   int indentLevel) {

        StringBuilder code = new StringBuilder();
        String indent = "    ".repeat(indentLevel);

        code.append(indent)
                .append("Map<String, Object> ")
                .append(varName)
                .append(" = Map.of(\n");

        int i = 0;
        for (Map.Entry<String, Object> entry : map.entrySet()) {

            String key = entry.getKey();
            Object value = entry.getValue();

            code.append(indent).append("    \"").append(key).append("\", ");

            if (value instanceof Map) {
                code.append(varName + "_" + key);
            } else if (value instanceof String) {
                code.append("\"").append(value).append("\"");
            } else {
                code.append(value);
            }

            if (i < map.size() - 1) {
                code.append(",");
            }

            code.append("\n");
            i++;
        }

        code.append(indent).append(");\n\n");

        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (entry.getValue() instanceof Map) {
                code.append(generateMapCode(
                        varName + "_" + entry.getKey(),
                        (Map<String, Object>) entry.getValue(),
                        indentLevel
                ));
            }
        }

        return code.toString();
    }

    private void saveToFile(String className, String code) {

        try {
            File folder = new File(testOutputPath);

            if (!folder.exists()) {
                folder.mkdirs();
            }

            File file = new File(folder, className + ".java");
            FileWriter writer = new FileWriter(file);
            writer.write(code);
            writer.close();

            System.out.println("Test file saved at: " + file.getAbsolutePath());

        } catch (Exception e) {
            throw new RuntimeException("Failed to write test file", e);
        }
    }

    private String formatMethodName(String testName) {
        return testName
                .replaceAll("[^a-zA-Z0-9]", "")
                .replaceFirst("^[0-9]", "test");
    }

    private String formatClassName(String endpoint) {

        String cleaned = endpoint
                .replaceAll("[{}]", "")
                .replaceAll("/", " ")
                .trim();

        String[] parts = cleaned.split(" ");
        StringBuilder className = new StringBuilder();

        for (String part : parts) {
            if (!part.isEmpty()) {
                className.append(Character.toUpperCase(part.charAt(0)))
                        .append(part.substring(1));
            }
        }

        className.append("ApiTests");
        return className.toString();
    }
}