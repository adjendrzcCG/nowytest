package com.example.support;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.Map;

/**
 * Thin wrapper around RestAssured that centralises base-URL configuration and
 * logging. All HTTP methods delegate to a pre-configured {@link RequestSpecification}.
 *
 * <p>The base URL is resolved from the system property {@code api.base.url},
 * falling back to {@code http://localhost:8080} when the property is absent.
 */
public class ApiClient {

    private static final String DEFAULT_BASE_URL = "http://localhost:8080";

    private final RequestSpecification requestSpec;

    public ApiClient() {
        String baseUrl = System.getProperty("api.base.url", DEFAULT_BASE_URL);

        requestSpec = new RequestSpecBuilder()
                .setBaseUri(baseUrl)
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .log(LogDetail.ALL)
                .build();

        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    // -------------------------------------------------------------------------
    // HTTP verbs
    // -------------------------------------------------------------------------

    /**
     * Sends a GET request to the given path.
     *
     * @param path URL path (may contain RestAssured path parameters)
     * @return the RestAssured {@link Response}
     */
    public Response get(String path) {
        return RestAssured.given()
                .spec(requestSpec)
                .when()
                .get(path)
                .then()
                .extract()
                .response();
    }

    /**
     * Sends a GET request with query parameters.
     *
     * @param path        URL path
     * @param queryParams query parameters to append
     * @return the RestAssured {@link Response}
     */
    public Response get(String path, Map<String, String> queryParams) {
        return RestAssured.given()
                .spec(requestSpec)
                .queryParams(queryParams)
                .when()
                .get(path)
                .then()
                .extract()
                .response();
    }

    /**
     * Sends a POST request with a JSON body.
     *
     * @param path        URL path
     * @param requestBody JSON request body string
     * @return the RestAssured {@link Response}
     */
    public Response post(String path, String requestBody) {
        return RestAssured.given()
                .spec(requestSpec)
                .body(requestBody)
                .when()
                .post(path)
                .then()
                .extract()
                .response();
    }

    /**
     * Sends a PUT request with a JSON body.
     *
     * @param path        URL path
     * @param requestBody JSON request body string
     * @return the RestAssured {@link Response}
     */
    public Response put(String path, String requestBody) {
        return RestAssured.given()
                .spec(requestSpec)
                .body(requestBody)
                .when()
                .put(path)
                .then()
                .extract()
                .response();
    }

    /**
     * Sends a PATCH request with a JSON body.
     *
     * @param path        URL path
     * @param requestBody JSON request body string
     * @return the RestAssured {@link Response}
     */
    public Response patch(String path, String requestBody) {
        return RestAssured.given()
                .spec(requestSpec)
                .body(requestBody)
                .when()
                .patch(path)
                .then()
                .extract()
                .response();
    }

    /**
     * Sends a DELETE request.
     *
     * @param path URL path
     * @return the RestAssured {@link Response}
     */
    public Response delete(String path) {
        return RestAssured.given()
                .spec(requestSpec)
                .when()
                .delete(path)
                .then()
                .extract()
                .response();
    }
}
