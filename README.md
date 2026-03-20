# AI QA Assistant

> An AI-powered productivity tool for QA engineers and SDETs — generates API test cases, test data, RestAssured code, and reviews existing test coverage using NVIDIA's llama3 AI model.

![Java](https://img.shields.io/badge/Java-17-orange?style=flat-square&logo=java)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.5-brightgreen?style=flat-square&logo=springboot)
![NVIDIA AI](https://img.shields.io/badge/NVIDIA-llama3--8b-76b900?style=flat-square&logo=nvidia)
![REST API](https://img.shields.io/badge/REST-API-blue?style=flat-square)
![License](https://img.shields.io/badge/license-MIT-purple?style=flat-square)

---

## What This Project Does

QA engineers spend hours writing test cases, generating test data, and analyzing failures manually. This tool automates those tasks using AI — giving engineers instant, comprehensive coverage with a single API call.

| Feature | Endpoint | What it does |
|---|---|---|
| API Test Generator | `POST /generate/api-tests` | Generates positive, negative, boundary and security test cases for any endpoint |
| Test Data Generator | `POST /generate/test-data` | Creates realistic test data including valid, invalid, boundary and edge case objects |
| RestAssured Code Generator | `POST /generate/restassured-tests` | Generates and saves a runnable `.java` RestAssured test file to disk |
| AI Test Reviewer | `POST /review/tests` | Reviews any existing test file and returns coverage score, gaps, and suggestions |

---

## Demo

### Generate API Tests
```bash
curl -X POST http://localhost:8080/generate/api-tests \
  -H "Content-Type: application/json" \
  -d '{
    "endpoint": "/login",
    "method": "POST",
    "schema": {
      "email": "string",
      "password": "string"
    }
  }'
```

**Response:**
```json
{
  "success": true,
  "message": "Test cases generated successfully",
  "data": [
    {
      "testName": "Valid Login",
      "description": "Positive: login with valid credentials",
      "input": { "email": "john@example.com", "password": "SecurePass@123" },
      "expectedResult": { "statusCode": 200, "responseBody": { "token": "jwt-token" } }
    },
    {
      "testName": "SQL Injection on Email",
      "description": "Security: attempt SQL injection via email field",
      "input": { "email": "admin@test.com OR 1=1", "password": "anything" },
      "expectedResult": { "statusCode": 400, "responseBody": {} }
    }
  ]
}
```

---

### Review Existing Tests
```bash
curl -X POST http://localhost:8080/review/tests \
  -H "Content-Type: application/json" \
  -d '{
    "framework": "Selenium",
    "endpoint": "/login",
    "testCode": "public void testValidLogin() { ... } public void testEmptyEmail() { ... }"
  }'
```

**Response:**
```json
{
  "success": true,
  "message": "Test review completed successfully",
  "data": {
    "totalTestsFound": 5,
    "coverageScore": "80%",
    "overallFeedback": "Good basic coverage but missing security and boundary scenarios.",
    "missingScenarios": [
      "No SQL injection test for email field",
      "No test for missing Authorization header",
      "No boundary test for password minimum length"
    ],
    "suggestions": [
      "Add test with email = 'admin@test.com OR 1=1'",
      "Add test with no Authorization header expecting 401"
    ],
    "goodPracticesFound": [
      "Good coverage of empty field scenarios",
      "Correct use of assertion methods"
    ]
  }
}
```

---

## Architecture

```
Client (Browser UI / Postman / curl)
            │
            ▼
  Spring Boot REST API
  (TestAssistantController)
            │
            ▼
     Service Layer
  ┌─────────────────────┐
  │  TestCaseService    │
  │  TestDataService    │
  │  CodeGeneratorSvc   │
  │  TestReviewerSvc    │
  └─────────────────────┘
            │
            ▼
       AiService
            │
            ▼
  NVIDIA API (llama3-8b-instruct)
```

---

## Project Structure

```
src/main/java/com/sakshi/ai_qa_assistant/
├── ai/
│   ├── AiService.java                  # Calls NVIDIA API
│   └── PromptTemplates.java            # All AI prompt templates
├── controller/
│   ├── TestAssistantController.java    # All REST endpoints
│   └── GlobalExceptionHandler.java     # Centralized error handling
├── entity/
│   ├── ApiTestRequest.java             # Input for test generation
│   ├── ApiTestCase.java                # Single test case model
│   ├── TestDataRequest.java            # Input for test data
│   ├── TestReviewRequest.java          # Input for test reviewer
│   ├── TestReviewResult.java           # Reviewer output model
│   └── ApiResponse.java               # Universal response wrapper
└── service/
    ├── TestCaseService.java            # Generates test cases via AI
    ├── TestDataService.java            # Generates test data via AI
    ├── CodeGeneratorService.java       # Writes .java files to disk
    └── TestReviewerService.java        # Reviews tests via AI

src/main/resources/
├── static/
│   └── index.html                     # Frontend UI (dark/light mode)
└── application.properties             # App configuration
```

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 3.3.5 |
| AI Model | NVIDIA llama3-8b-instruct |
| HTTP Client | Spring RestTemplate |
| JSON Processing | Jackson ObjectMapper |
| Test Framework | JUnit 5 + RestAssured 5.4 |
| Build Tool | Maven |
| Frontend | HTML + CSS + Vanilla JS (single file) |

---

## Getting Started

### Prerequisites

- Java 17+
- Maven
- IntelliJ IDEA (recommended)
- NVIDIA API key — get one free at [build.nvidia.com](https://build.nvidia.com)

### 1. Clone the repository

```bash
git clone https://github.com/SakshiKhandare/ai-qa-assistant.git
cd ai-qa-assistant
```

### 2. Set up your API key

Create a `.env` file in the project root:

```
NVIDIA_API_KEY=your_nvidia_api_key_here
```

Or set it directly in your IDE's run configuration as an environment variable:
```
NVIDIA_API_KEY=your_nvidia_api_key_here
```

> ⚠️ Never commit your API key. The `.env` file is already in `.gitignore`.

### 3. Run the application

```bash
./mvnw spring-boot:run
```

Or run `AiQaAssistantApplication.java` directly from IntelliJ.

### 4. Open the UI

```
http://localhost:8080
```

---

## API Reference

### POST /generate/api-tests

Generates comprehensive test cases for a given API endpoint.

**Request body:**
```json
{
  "endpoint": "/users/register",
  "method": "POST",
  "schema": {
    "name": "string",
    "email": "string",
    "password": "string",
    "age": "number"
  }
}
```

**Covers:** positive scenarios, negative scenarios, boundary values, security tests (SQL injection, XSS, auth bypass), HTTP method-specific cases.

---

### POST /generate/test-data

Generates realistic test data objects from a schema.

**Request body:**
```json
{
  "schema": {
    "name": "string",
    "email": "string",
    "age": "number"
  }
}
```

**Returns:** 5 objects — mix of valid, invalid, boundary, and edge case data.

---

### POST /generate/restassured-tests

Generates a complete, runnable RestAssured test file and saves it to `src/test/java/generated/`.

**Request body:** Same as `/generate/api-tests`

---

### POST /review/tests

Reviews an existing test file and returns a detailed coverage analysis.

**Request body:**
```json
{
  "framework": "Selenium",
  "endpoint": "/login",
  "testCode": "paste your full test file content here"
}
```

**Supports:** RestAssured, Selenium, Pytest, Cypress, JUnit — any framework.

---

## Response Format

All endpoints return a consistent response wrapper:

```json
{
  "success": true,
  "message": "Human readable message",
  "data": { }
}
```

On error:
```json
{
  "success": false,
  "message": "Endpoint cannot be empty",
  "data": null
}
```

---

## Key Design Decisions

**Retry logic** — `TestCaseService` retries the AI call up to 2 times if the response cannot be parsed as valid JSON, handling AI model inconsistencies gracefully.

**Prompt engineering** — prompts are strictly structured to return only JSON with no markdown, enforcing single array output, minimum test counts, and realistic values to ensure parseable, high-quality responses every time.

**Configurable output path** — the path where generated `.java` files are saved is externalized to `application.properties` via `test.output.path`, making it easy to change without touching code.

**Centralized error handling** — `GlobalExceptionHandler` catches all exceptions across the app and returns clean, structured error responses instead of Spring's default stack trace output.

---

## Environment Variables

| Variable | Description | Required |
|---|---|---|
| `NVIDIA_API_KEY` | Your NVIDIA API key from build.nvidia.com | Yes |

---

## Configuration

`src/main/resources/application.properties`:

```properties
spring.application.name=ai-qa-assistant
nvidia.api.key=${NVIDIA_API_KEY}
test.output.path=src/test/java/generated
```

---

## What This Project Demonstrates

- **API testing expertise** — deep understanding of test case design, coverage types, and QA best practices
- **AI integration** — calling and parsing responses from a real LLM API with prompt engineering
- **Java backend development** — Spring Boot, REST APIs, service layers, dependency injection
- **Developer tooling mindset** — building tools that solve real problems for other engineers
- **Production thinking** — error handling, input validation, configurable settings, clean architecture

---

## Author

**Sakshi Khandare**

[![GitHub](https://img.shields.io/badge/GitHub-SakshiKhandare-181717?style=flat-square&logo=github)](https://github.com/SakshiKhandare)

---

## License

This project is licensed under the MIT License.
