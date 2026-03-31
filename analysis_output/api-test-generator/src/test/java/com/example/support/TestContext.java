package com.example.support;

import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

/**
 * Shared test context that is passed between Cucumber step definitions via
 * PicoContainer dependency injection. Stores the last HTTP response, dynamic
 * path variables resolved from stored IDs, and arbitrary key-value data
 * accumulated during a scenario.
 */
public class TestContext {

    /** The most recent RestAssured {@link Response}. */
    private Response lastResponse;

    /** Generic data bag used to share values between steps (e.g. stored IDs). */
    private final Map<String, String> scenarioData = new HashMap<>();

    /** The request body that will be sent in the next write operation. */
    private String requestBody;

    // -------------------------------------------------------------------------
    // Response
    // -------------------------------------------------------------------------

    public Response getLastResponse() {
        return lastResponse;
    }

    public void setLastResponse(Response response) {
        this.lastResponse = response;
    }

    // -------------------------------------------------------------------------
    // Scenario data store
    // -------------------------------------------------------------------------

    public void store(String key, String value) {
        scenarioData.put(key, value);
    }

    public String retrieve(String key) {
        return scenarioData.get(key);
    }

    public boolean has(String key) {
        return scenarioData.containsKey(key);
    }

    public Map<String, String> getAllData() {
        return scenarioData;
    }

    /**
     * Replaces all occurrences of <code>{key}</code> in the given text with the
     * corresponding stored value, enabling dynamic path/body substitution.
     *
     * @param text the template string
     * @return the resolved string
     */
    public String resolve(String text) {
        if (text == null) {
            return null;
        }
        String result = text;
        for (Map.Entry<String, String> entry : scenarioData.entrySet()) {
            result = result.replace("{" + entry.getKey() + "}", entry.getValue());
        }
        return result;
    }

    // -------------------------------------------------------------------------
    // Request body
    // -------------------------------------------------------------------------

    public String getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(String body) {
        this.requestBody = body;
    }

    /** Clears the request body so it is not accidentally reused between steps. */
    public void clearRequestBody() {
        this.requestBody = null;
    }

    // -------------------------------------------------------------------------
    // Lifecycle
    // -------------------------------------------------------------------------

    /** Resets all mutable state. Called from Cucumber hooks before each scenario. */
    public void reset() {
        lastResponse = null;
        requestBody = null;
        scenarioData.clear();
    }
}
