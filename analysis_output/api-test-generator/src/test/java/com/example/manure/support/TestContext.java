package com.example.manure.support;

import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

/**
 * TestContext holds shared state between Cucumber step definitions within a single scenario.
 * A new instance is created for each scenario via Cucumber's PicoContainer dependency injection.
 */
public class TestContext {

    private final ApiClient apiClient;
    private Response response;
    private Object requestBody;
    private String authToken;
    private String currentResourceId;
    private final Map<String, Object> store = new HashMap<>();

    public TestContext() {
        String baseUrl = System.getProperty("api.base.url",
            System.getenv().getOrDefault("API_BASE_URL", "http://localhost:8080"));
        this.apiClient = new ApiClient(baseUrl);
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public Object getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(Object requestBody) {
        this.requestBody = requestBody;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
        apiClient.setAuthToken(authToken);
    }

    public String getCurrentResourceId() {
        return currentResourceId;
    }

    public void setCurrentResourceId(String currentResourceId) {
        this.currentResourceId = currentResourceId;
    }

    /**
     * Stores an arbitrary value by key for use across step definitions within the same scenario.
     */
    public void put(String key, Object value) {
        store.put(key, value);
    }

    /**
     * Retrieves a value stored by key, casting to the specified type.
     */
    @SuppressWarnings("unchecked")
    public <T> T get(String key, Class<T> type) {
        return (T) store.get(key);
    }

    public Object get(String key) {
        return store.get(key);
    }
}
