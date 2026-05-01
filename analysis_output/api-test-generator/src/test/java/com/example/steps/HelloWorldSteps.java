package com.example.steps;

import com.example.support.ApiClient;
import com.example.support.TestContext;
import com.example.support.TestDataBuilder;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.junit.Assert;

import java.util.List;

/**
 * Cucumber step definitions for the HelloWorld Greeting API feature.
 *
 * <p>All steps share state through the {@link TestContext} which is injected
 * by PicoContainer.
 */
public class HelloWorldSteps {

    private final TestContext context;
    private final ApiClient apiClient;

    public HelloWorldSteps(TestContext context) {
        this.context = context;
        this.apiClient = new ApiClient();
    }

    // -------------------------------------------------------------------------
    // Given
    // -------------------------------------------------------------------------

    @Given("the Greeting API is available at base URL")
    public void theGreetingApiIsAvailable() {
        // The ApiClient reads api.base.url system property; this step is a marker
        // for readability and validates connectivity via a lightweight health check.
        Response response = apiClient.get("/api/health");
        Assert.assertTrue(
                "Greeting API should respond with 2xx on /api/health, got: " + response.statusCode(),
                response.statusCode() < 300
        );
    }

    @Given("greetings exist in the system")
    public void greetingsExistInTheSystem() {
        // Seed a greeting so that "get all" scenarios have data
        String body = TestDataBuilder.defaultGreetingBody("SeedUser");
        Response response = apiClient.post("/api/greet", body);
        Assert.assertTrue(
                "Failed to seed greeting, status: " + response.statusCode(),
                response.statusCode() == 200 || response.statusCode() == 201
        );
    }

    @Given("I have a greeting request body:")
    public void iHaveAGreetingRequestBody(String docString) {
        context.setRequestBody(context.resolve(docString.trim()));
    }

    @Given("I have an update request body:")
    public void iHaveAnUpdateRequestBody(String docString) {
        context.setRequestBody(context.resolve(docString.trim()));
    }

    @Given("I have a batch update request body:")
    public void iHaveABatchUpdateRequestBody(String docString) {
        context.setRequestBody(context.resolve(docString.trim()));
    }

    // -------------------------------------------------------------------------
    // When
    // -------------------------------------------------------------------------

    @When("I send a GET request to {string}")
    public void iSendAGetRequestTo(String path) {
        String resolvedPath = context.resolve(path);
        Response response = apiClient.get(resolvedPath);
        context.setLastResponse(response);
    }

    @When("I send a POST request to {string}")
    public void iSendAPostRequestTo(String path) {
        String resolvedPath = context.resolve(path);
        String body = context.getRequestBody();
        Assert.assertNotNull("Request body must be set before sending POST", body);
        Response response = apiClient.post(resolvedPath, body);
        context.setLastResponse(response);
        context.clearRequestBody();
    }

    @When("I send a PUT request to {string}")
    public void iSendAPutRequestTo(String path) {
        String resolvedPath = context.resolve(path);
        String body = context.getRequestBody();
        Assert.assertNotNull("Request body must be set before sending PUT", body);
        Response response = apiClient.put(resolvedPath, body);
        context.setLastResponse(response);
        context.clearRequestBody();
    }

    @When("I send a PATCH request to {string}")
    public void iSendAPatchRequestTo(String path) {
        String resolvedPath = context.resolve(path);
        String body = context.getRequestBody();
        Assert.assertNotNull("Request body must be set before sending PATCH", body);
        Response response = apiClient.patch(resolvedPath, body);
        context.setLastResponse(response);
        context.clearRequestBody();
    }

    @When("I send a DELETE request to {string}")
    public void iSendADeleteRequestTo(String path) {
        String resolvedPath = context.resolve(path);
        Response response = apiClient.delete(resolvedPath);
        context.setLastResponse(response);
    }

    // -------------------------------------------------------------------------
    // Then
    // -------------------------------------------------------------------------

    @Then("the response status code should be {int}")
    public void theResponseStatusCodeShouldBe(int expectedStatus) {
        Response response = context.getLastResponse();
        Assert.assertNotNull("No response available – did you send a request?", response);
        Assert.assertEquals(
                "Unexpected HTTP status code. Response body: " + response.asString(),
                expectedStatus,
                response.statusCode()
        );
    }

    @Then("the response body should contain {string}")
    public void theResponseBodyShouldContain(String expected) {
        String body = context.getLastResponse().asString();
        Assert.assertTrue(
                "Expected response body to contain '" + expected + "' but was: " + body,
                body.contains(expected)
        );
    }

    @Then("the response content type should be {string}")
    public void theResponseContentTypeShouldBe(String expectedContentType) {
        String actualContentType = context.getLastResponse().contentType();
        Assert.assertTrue(
                "Expected content type '" + expectedContentType + "' but was: " + actualContentType,
                actualContentType != null && actualContentType.contains(expectedContentType)
        );
    }

    @Then("the response JSON field {string} should equal {string}")
    public void theResponseJsonFieldShouldEqual(String jsonPath, String expectedValue) {
        String actual = context.getLastResponse().jsonPath().getString(jsonPath);
        Assert.assertEquals(
                "JSON field '" + jsonPath + "' mismatch",
                expectedValue,
                actual
        );
    }

    @Then("the response JSON field {string} should contain {string}")
    public void theResponseJsonFieldShouldContain(String jsonPath, String substring) {
        String actual = context.getLastResponse().jsonPath().getString(jsonPath);
        Assert.assertNotNull("JSON field '" + jsonPath + "' was null", actual);
        Assert.assertTrue(
                "Expected JSON field '" + jsonPath + "' to contain '" + substring + "' but was: " + actual,
                actual.contains(substring)
        );
    }

    @Then("the response JSON field {string} should not be empty")
    public void theResponseJsonFieldShouldNotBeEmpty(String jsonPath) {
        String actual = context.getLastResponse().jsonPath().getString(jsonPath);
        Assert.assertNotNull("JSON field '" + jsonPath + "' should not be null", actual);
        Assert.assertFalse("JSON field '" + jsonPath + "' should not be empty", actual.trim().isEmpty());
    }

    @Then("the response body should be a JSON array")
    public void theResponseBodyShouldBeAJsonArray() {
        String body = context.getLastResponse().asString().trim();
        Assert.assertTrue(
                "Expected response to be a JSON array (starts with '['), but was: " + body,
                body.startsWith("[")
        );
    }

    @Then("the response JSON array should have at least {int} element")
    public void theResponseJsonArrayShouldHaveAtLeastElements(int minCount) {
        List<?> list = context.getLastResponse().jsonPath().getList("$");
        Assert.assertNotNull("JSON array was null", list);
        Assert.assertTrue(
                "Expected at least " + minCount + " elements but found: " + list.size(),
                list.size() >= minCount
        );
    }

    // -------------------------------------------------------------------------
    // And (re-used as aliases)
    // -------------------------------------------------------------------------

    @And("I store the response JSON field {string} as {string}")
    public void iStoreTheResponseJsonFieldAs(String jsonPath, String key) {
        String value = context.getLastResponse().jsonPath().getString(jsonPath);
        Assert.assertNotNull("Cannot store null value for JSON field '" + jsonPath + "'", value);
        context.store(key, value);
    }

    @And("the response JSON field {string} should equal the stored value {string}")
    public void theResponseJsonFieldShouldEqualStoredValue(String jsonPath, String storedKey) {
        String expected = context.retrieve(storedKey);
        String actual = context.getLastResponse().jsonPath().getString(jsonPath);
        Assert.assertEquals(
                "JSON field '" + jsonPath + "' should equal stored value for key '" + storedKey + "'",
                expected,
                actual
        );
    }

    @And("the response JSON field {string} should not equal the stored value {string}")
    public void theResponseJsonFieldShouldNotEqualStoredValue(String jsonPath, String storedKey) {
        String notExpected = context.retrieve(storedKey);
        String actual = context.getLastResponse().jsonPath().getString(jsonPath);
        Assert.assertNotEquals(
                "JSON field '" + jsonPath + "' should NOT equal stored value for key '" + storedKey + "'",
                notExpected,
                actual
        );
    }
}
