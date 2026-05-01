package com.example.manure.steps;

import com.example.manure.support.ApiClient;
import com.example.manure.support.TestContext;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.junit.Assert;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.*;

public class ManureStorageSteps {

    private final TestContext context;
    private final ApiClient apiClient;

    public ManureStorageSteps(TestContext context) {
        this.context = context;
        this.apiClient = context.getApiClient();
    }

    @Given("the Manure API is available")
    public void theManureApiIsAvailable() {
        Response response = apiClient.get("/api/v1/health");
        Assert.assertEquals("API health check failed", 200, response.getStatusCode());
    }

    @Given("I am authenticated as a farm manager")
    public void iAmAuthenticatedAsAFarmManager() {
        String token = apiClient.authenticate("farm_manager", "password123");
        context.setAuthToken(token);
    }

    @Given("I have valid storage facility details")
    public void iHaveValidStorageFacilityDetails(DataTable dataTable) {
        Map<String, String> data = dataTable.asMap(String.class, String.class);
        context.setRequestBody(buildStorageFacilityPayload(data));
    }

    @Given("I have valid storage facility details for type {string}")
    public void iHaveValidStorageFacilityDetailsForType(String manureType) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("name", "Test Facility " + manureType);
        payload.put("type", manureType);
        payload.put("capacityM3", 300);
        payload.put("location", "Test Location");
        payload.put("constructionDate", "2022-01-01");
        context.setRequestBody(payload);
    }

    @Given("a storage facility with ID {string} exists")
    public void aStorageFacilityWithIDExists(String facilityId) {
        Response response = apiClient.get("/api/v1/storage-facilities/" + facilityId);
        if (response.getStatusCode() == 404) {
            Map<String, Object> payload = new HashMap<>();
            payload.put("id", facilityId);
            payload.put("name", "Test Facility " + facilityId);
            payload.put("type", "SLURRY");
            payload.put("capacityM3", 500);
            payload.put("location", "Test Location");
            payload.put("constructionDate", "2020-01-01");
            Response createResponse = apiClient.post("/api/v1/storage-facilities", payload);
            Assert.assertEquals(201, createResponse.getStatusCode());
        }
        context.setCurrentResourceId(facilityId);
    }

    @Given("there are {int} storage facilities in the system")
    public void thereAreStorageFacilitiesInTheSystem(int count) {
        for (int i = 0; i < count; i++) {
            Map<String, Object> payload = new HashMap<>();
            payload.put("name", "Auto Facility " + i);
            payload.put("type", "SLURRY");
            payload.put("capacityM3", 200 + i * 100);
            payload.put("location", "Location " + i);
            payload.put("constructionDate", "2021-01-01");
            apiClient.post("/api/v1/storage-facilities", payload);
        }
    }

    @Given("I have updated storage capacity details")
    public void iHaveUpdatedStorageCapacityDetails(DataTable dataTable) {
        Map<String, String> data = dataTable.asMap(String.class, String.class);
        Map<String, Object> payload = new HashMap<>();
        for (Map.Entry<String, String> entry : data.entrySet()) {
            if (entry.getKey().endsWith("M3") || entry.getKey().endsWith("Percent")) {
                payload.put(entry.getKey(), Double.parseDouble(entry.getValue()));
            } else {
                payload.put(entry.getKey(), entry.getValue());
            }
        }
        context.setRequestBody(payload);
    }

    @Given("I have incomplete storage facility details with missing {string}")
    public void iHaveIncompleteStorageFacilityDetailsWithMissing(String missingField) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("type", "SLURRY");
        payload.put("capacityM3", 300);
        payload.put("location", "Test Location");
        // Intentionally omit the field specified
        if (!"name".equals(missingField)) {
            payload.put("name", "Test Facility");
        }
        context.setRequestBody(payload);
    }

    @When("I send a GET request to {string}")
    public void iSendAGetRequestTo(String path) {
        Response response = apiClient.get(path);
        context.setResponse(response);
    }

    @When("I send a POST request to {string}")
    public void iSendAPostRequestTo(String path) {
        Response response = apiClient.post(path, context.getRequestBody());
        context.setResponse(response);
    }

    @When("I send a POST request to {string} with body")
    public void iSendAPostRequestToWithBody(String path, String body) {
        Response response = apiClient.postRaw(path, body);
        context.setResponse(response);
    }

    @When("I send a PUT request to {string}")
    public void iSendAPutRequestTo(String path) {
        Response response = apiClient.put(path, context.getRequestBody());
        context.setResponse(response);
    }

    @When("I send a DELETE request to {string}")
    public void iSendADeleteRequestTo(String path) {
        Response response = apiClient.delete(path);
        context.setResponse(response);
    }

    @Then("the response status code should be {int}")
    public void theResponseStatusCodeShouldBe(int expectedStatus) {
        Response response = context.getResponse();
        Assert.assertEquals(
            "Expected status " + expectedStatus + " but got " + response.getStatusCode()
                + ". Response body: " + response.getBody().asString(),
            expectedStatus,
            response.getStatusCode()
        );
    }

    @Then("the response body should contain field {string}")
    public void theResponseBodyShouldContainField(String field) {
        context.getResponse().then().body(field, notNullValue());
    }

    @Then("the response body should contain field {string} with value {string}")
    public void theResponseBodyShouldContainFieldWithValue(String field, String value) {
        String actual = context.getResponse().jsonPath().getString(field);
        Assert.assertEquals("Field '" + field + "' mismatch", value, actual);
    }

    @Then("the response body should be a list with at least {int} items")
    public void theResponseBodyShouldBeAListWithAtLeastItems(int minCount) {
        List<?> list = context.getResponse().jsonPath().getList("$");
        Assert.assertNotNull("Response body is not a list", list);
        Assert.assertTrue(
            "Expected at least " + minCount + " items but got " + list.size(),
            list.size() >= minCount
        );
    }

    @And("the response content type should be {string}")
    public void theResponseContentTypeShouldBe(String contentType) {
        String actual = context.getResponse().getContentType();
        Assert.assertTrue(
            "Expected content type '" + contentType + "' but got '" + actual + "'",
            actual != null && actual.startsWith(contentType)
        );
    }

    @And("the response body list {string} should not be empty")
    public void theResponseBodyListShouldNotBeEmpty(String listField) {
        List<?> list = context.getResponse().jsonPath().getList(listField);
        Assert.assertNotNull("List field '" + listField + "' is null", list);
        Assert.assertFalse("List field '" + listField + "' is empty", list.isEmpty());
    }

    private Map<String, Object> buildStorageFacilityPayload(Map<String, String> data) {
        Map<String, Object> payload = new HashMap<>();
        for (Map.Entry<String, String> entry : data.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if ("capacityM3".equals(key)) {
                payload.put(key, Integer.parseInt(value));
            } else {
                payload.put(key, value);
            }
        }
        return payload;
    }
}
