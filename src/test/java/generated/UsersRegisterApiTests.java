package generated;

import static io.restassured.RestAssured.*;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.util.Map;

public class UsersRegisterApiTests {

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "http://localhost:8080";
    }

    @Test
    public void ValidRegistration() {

        Map<String, Object> requestBody = Map.of(
            "name", "John Doe",
            "email", "john.doe@example.com",
            "password", "password123",
            "age", "30"
        );

        given()
            .body(requestBody)
        .when()
            .post("/users/register")
        .then()
            .statusCode(200);

    }

    @Test
    public void InvalidEmail() {

        Map<String, Object> requestBody = Map.of(
            "name", "John Doe",
            "email", "invalidemail",
            "password", "password123",
            "age", "30"
        );

        given()
            .body(requestBody)
        .when()
            .post("/users/register")
        .then()
            .statusCode(400);

    }

    @Test
    public void InvalidPassword() {

        Map<String, Object> requestBody = Map.of(
            "name", "John Doe",
            "email", "john.doe@example.com",
            "password", "short",
            "age", "30"
        );

        given()
            .body(requestBody)
        .when()
            .post("/users/register")
        .then()
            .statusCode(400);

    }

    @Test
    public void MissingName() {

        Map<String, Object> requestBody = Map.of(
            "email", "john.doe@example.com",
            "password", "password123",
            "age", "30"
        );

        given()
            .body(requestBody)
        .when()
            .post("/users/register")
        .then()
            .statusCode(400);

    }

    @Test
    public void MissingEmail() {

        Map<String, Object> requestBody = Map.of(
            "name", "John Doe",
            "password", "password123",
            "age", "30"
        );

        given()
            .body(requestBody)
        .when()
            .post("/users/register")
        .then()
            .statusCode(400);

    }

    @Test
    public void MissingPassword() {

        Map<String, Object> requestBody = Map.of(
            "name", "John Doe",
            "email", "john.doe@example.com",
            "age", "30"
        );

        given()
            .body(requestBody)
        .when()
            .post("/users/register")
        .then()
            .statusCode(400);

    }

    @Test
    public void InvalidAge() {

        Map<String, Object> requestBody = Map.of(
            "name", "John Doe",
            "email", "john.doe@example.com",
            "password", "password123",
            "age", "-1"
        );

        given()
            .body(requestBody)
        .when()
            .post("/users/register")
        .then()
            .statusCode(400);

    }

    @Test
    public void ValidRegistrationwithwhitespace() {

        Map<String, Object> requestBody = Map.of(
            "name", "  John Doe  ",
            "email", "john.doe@example.com",
            "password", "password123",
            "age", "30"
        );

        given()
            .body(requestBody)
        .when()
            .post("/users/register")
        .then()
            .statusCode(200);

    }

}