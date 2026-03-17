import static io.restassured.RestAssured.*;
import org.junit.jupiter.api.Test;
import java.util.Map;

public class GeneratedApiTests {

    @Test
    public void ValidLogin() {

        Map<String, Object> requestBody = Map.of(
            "email", "user@example.com",
            "password", "password123"
        );

        given()
            .body(requestBody)
        .when()
            .post("/login")
        .then()
            .statusCode(200);

    }

    @Test
    public void InvalidEmail() {

        Map<String, Object> requestBody = Map.of(
            "email", "invalidemail",
            "password", "password123"
        );

        given()
            .body(requestBody)
        .when()
            .post("/login")
        .then()
            .statusCode(400);

    }

    @Test
    public void InvalidPassword() {

        Map<String, Object> requestBody = Map.of(
            "email", "user@example.com",
            "password", "invalidpassword"
        );

        given()
            .body(requestBody)
        .when()
            .post("/login")
        .then()
            .statusCode(400);

    }

    @Test
    public void EmptyEmail() {

        Map<String, Object> requestBody = Map.of(
            "email", "",
            "password", "password123"
        );

        given()
            .body(requestBody)
        .when()
            .post("/login")
        .then()
            .statusCode(400);

    }

    @Test
    public void EmptyPassword() {

        Map<String, Object> requestBody = Map.of(
            "email", "user@example.com",
            "password", ""
        );

        given()
            .body(requestBody)
        .when()
            .post("/login")
        .then()
            .statusCode(400);

    }

    @Test
    public void MissingEmail() {

        Map<String, Object> requestBody = Map.of(
            "password", "password123"
        );

        given()
            .body(requestBody)
        .when()
            .post("/login")
        .then()
            .statusCode(400);

    }

    @Test
    public void MissingPassword() {

        Map<String, Object> requestBody = Map.of(
            "email", "user@example.com"
        );

        given()
            .body(requestBody)
        .when()
            .post("/login")
        .then()
            .statusCode(400);

    }

    @Test
    public void InvalidEmailFormat() {

        Map<String, Object> requestBody = Map.of(
            "email", "invalidemail.com",
            "password", "password123"
        );

        given()
            .body(requestBody)
        .when()
            .post("/login")
        .then()
            .statusCode(400);

    }

}