package com.example.manure.support;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.Map;

/**
 * ApiClient wraps RestAssured to provide a convenient HTTP client for API tests.
 * All requests are pre-configured with base URI and authentication.
 */
public class ApiClient {

    private final String baseUrl;
    private String authToken;

    public ApiClient(String baseUrl) {
        this.baseUrl = baseUrl;
        RestAssured.baseURI = baseUrl;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    /**
     * Authenticates with the API and returns a JWT bearer token.
     */
    public String authenticate(String username, String password) {
        Map<String, String> credentials = Map.of("username", username, "password", password);
        Response response = RestAssured
            .given()
                .contentType(ContentType.JSON)
                .body(credentials)
            .when()
                .post("/api/v1/auth/login")
            .then()
                .statusCode(200)
                .extract().response();
        String token = response.jsonPath().getString("accessToken");
        this.authToken = token;
        return token;
    }

    public void setAuthToken(String token) {
        this.authToken = token;
    }

    /**
     * Performs an HTTP GET request to the given path.
     */
    public Response get(String path) {
        return buildRequest()
            .when()
                .get(path)
            .then()
                .extract().response();
    }

    /**
     * Performs an HTTP POST request with a JSON payload.
     */
    public Response post(String path, Object body) {
        return buildRequest()
                .contentType(ContentType.JSON)
                .body(body)
            .when()
                .post(path)
            .then()
                .extract().response();
    }

    /**
     * Performs an HTTP POST request with a raw JSON string body.
     */
    public Response postRaw(String path, String rawBody) {
        return buildRequest()
                .contentType(ContentType.JSON)
                .body(rawBody)
            .when()
                .post(path)
            .then()
                .extract().response();
    }

    /**
     * Performs an HTTP PUT request with a JSON payload.
     */
    public Response put(String path, Object body) {
        return buildRequest()
                .contentType(ContentType.JSON)
                .body(body)
            .when()
                .put(path)
            .then()
                .extract().response();
    }

    /**
     * Performs an HTTP DELETE request.
     */
    public Response delete(String path) {
        return buildRequest()
            .when()
                .delete(path)
            .then()
                .extract().response();
    }

    /**
     * Performs an HTTP PATCH request with a JSON payload.
     */
    public Response patch(String path, Object body) {
        return buildRequest()
                .contentType(ContentType.JSON)
                .body(body)
            .when()
                .patch(path)
            .then()
                .extract().response();
    }

    private RequestSpecification buildRequest() {
        RequestSpecification spec = RestAssured.given()
            .accept(ContentType.JSON);
        if (authToken != null && !authToken.isEmpty()) {
            spec = spec.header("Authorization", "Bearer " + authToken);
        }
        return spec;
    }
}
