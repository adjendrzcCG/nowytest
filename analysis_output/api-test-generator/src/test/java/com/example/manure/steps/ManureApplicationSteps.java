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

public class ManureApplicationSteps {

    private final TestContext context;
    private final ApiClient apiClient;

    public ManureApplicationSteps(TestContext context) {
        this.context = context;
        this.apiClient = context.getApiClient();
    }

    @Given("a field with ID {string} exists")
    public void aFieldWithIDExists(String fieldId) {
        Response response = apiClient.get("/api/v1/fields/" + fieldId);
        if (response.getStatusCode() == 404) {
            Map<String, Object> payload = new HashMap<>();
            payload.put("id", fieldId);
            payload.put("name", "Test Field " + fieldId);
            payload.put("areaHectares", 10.0);
            payload.put("soilType", "CLAY_LOAM");
            payload.put("nitrateZone", "NVZ");
            payload.put("cropType", "WINTER_WHEAT");
            payload.put("farmId", "FARM001");
            Response createResponse = apiClient.post("/api/v1/fields", payload);
            Assert.assertEquals(201, createResponse.getStatusCode());
        }
        context.setCurrentResourceId(fieldId);
    }

    @Given("I have valid application details")
    public void iHaveValidApplicationDetails(DataTable dataTable) {
        Map<String, String> data = dataTable.asMap(String.class, String.class);
        context.setRequestBody(buildApplicationPayload(data));
    }

    @Given("I have valid application details with method {string}")
    public void iHaveValidApplicationDetailsWithMethod(String method) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("storageId", "SF001");
        payload.put("fieldId", "FIELD001");
        payload.put("applicationDate", "2024-04-15");
        payload.put("volumeM3", 20.0);
        payload.put("applicationMethod", method);
        payload.put("cropType", "WINTER_WHEAT");
        context.setRequestBody(payload);
    }

    @Given("an application with ID {string} exists")
    public void anApplicationWithIDExists(String applicationId) {
        Response response = apiClient.get("/api/v1/applications/" + applicationId);
        if (response.getStatusCode() == 404) {
            Map<String, Object> payload = new HashMap<>();
            payload.put("id", applicationId);
            payload.put("storageId", "SF001");
            payload.put("fieldId", "FIELD001");
            payload.put("applicationDate", "2024-04-01");
            payload.put("volumeM3", 25.0);
            payload.put("applicationMethod", "TRAILING_SHOE");
            payload.put("cropType", "WINTER_WHEAT");
            Response createResponse = apiClient.post("/api/v1/applications", payload);
            Assert.assertEquals(201, createResponse.getStatusCode());
        }
        context.setCurrentResourceId(applicationId);
    }

    @Given("an application with ID {string} exists with status {string}")
    public void anApplicationWithIDExistsWithStatus(String applicationId, String status) {
        anApplicationWithIDExists(applicationId);
        // If we need a specific status, ensure it's in the right state
        if ("DRAFT".equals(status)) {
            // Applications should start as DRAFT by default
            Response response = apiClient.get("/api/v1/applications/" + applicationId);
            String currentStatus = response.jsonPath().getString("status");
            if (!"DRAFT".equals(currentStatus)) {
                // Create a fresh one
                Map<String, Object> payload = new HashMap<>();
                payload.put("storageId", "SF001");
                payload.put("fieldId", "FIELD001");
                payload.put("applicationDate", "2024-05-01");
                payload.put("volumeM3", 15.0);
                payload.put("applicationMethod", "BROADCAST");
                payload.put("cropType", "SPRING_BARLEY");
                Response createResponse = apiClient.post("/api/v1/applications", payload);
                String newId = createResponse.jsonPath().getString("id");
                context.setCurrentResourceId(newId);
            }
        }
    }

    @Given("field {string} has {int} recorded applications")
    public void fieldHasRecordedApplications(String fieldId, int count) {
        for (int i = 0; i < count; i++) {
            Map<String, Object> payload = new HashMap<>();
            payload.put("storageId", "SF001");
            payload.put("fieldId", fieldId);
            payload.put("applicationDate", "2024-03-" + String.format("%02d", i + 1));
            payload.put("volumeM3", 10.0 + i);
            payload.put("applicationMethod", "TRAILING_SHOE");
            payload.put("cropType", "WINTER_WHEAT");
            apiClient.post("/api/v1/applications", payload);
        }
    }

    @Given("there are applications recorded in March 2024")
    public void thereAreApplicationsRecordedInMarch2024() {
        for (int i = 1; i <= 2; i++) {
            Map<String, Object> payload = new HashMap<>();
            payload.put("storageId", "SF001");
            payload.put("fieldId", "FIELD001");
            payload.put("applicationDate", "2024-03-" + String.format("%02d", i * 5));
            payload.put("volumeM3", 20.0);
            payload.put("applicationMethod", "BAND_SPREADING");
            payload.put("cropType", "WINTER_WHEAT");
            apiClient.post("/api/v1/applications", payload);
        }
    }

    @Given("I have updated application details")
    public void iHaveUpdatedApplicationDetails(DataTable dataTable) {
        Map<String, String> data = dataTable.asMap(String.class, String.class);
        Map<String, Object> payload = new HashMap<>();
        for (Map.Entry<String, String> entry : data.entrySet()) {
            try {
                payload.put(entry.getKey(), Double.parseDouble(entry.getValue()));
            } catch (NumberFormatException e) {
                payload.put(entry.getKey(), entry.getValue());
            }
        }
        context.setRequestBody(payload);
    }

    @Given("a field with ID {string} has a maximum nitrogen allowance of {int} kg/ha")
    public void aFieldHasMaximumNitrogenAllowance(String fieldId, int allowance) {
        aFieldWithIDExists(fieldId);
        // Update field nitrogen allowance
        Map<String, Object> update = new HashMap<>();
        update.put("maxNitrogenAllowanceKgPerHa", allowance);
        apiClient.put("/api/v1/fields/" + fieldId, update);
    }

    @Given("the field has already received applications this year totalling {int} kg/ha")
    public void theFieldHasAlreadyReceivedApplications(int totalKgPerHa) {
        context.put("existingNitrogenKgPerHa", totalKgPerHa);
    }

    @When("I send a POST request to {string} with volume exceeding allowance")
    public void iSendAPostRequestWithVolumeExceedingAllowance(String path) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("storageId", "SF001");
        payload.put("fieldId", "FIELD001");
        payload.put("applicationDate", "2024-04-15");
        payload.put("volumeM3", 999.0); // Exceeds allowance
        payload.put("applicationMethod", "BROADCAST");
        payload.put("cropType", "WINTER_WHEAT");
        Response response = apiClient.post(path, payload);
        context.setResponse(response);
    }

    @Given("the current date is in a closed application period")
    public void theCurrentDateIsInAClosedApplicationPeriod() {
        context.put("closedSeasonDate", "2024-01-15"); // Typically closed Oct-Jan in UK
    }

    @When("I send a POST request to {string} with a closed season date")
    public void iSendAPostRequestWithAClosedSeasonDate(String path) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("storageId", "SF001");
        payload.put("fieldId", "FIELD001");
        payload.put("applicationDate", context.get("closedSeasonDate", String.class));
        payload.put("volumeM3", 20.0);
        payload.put("applicationMethod", "BROADCAST");
        payload.put("cropType", "WINTER_WHEAT");
        Response response = apiClient.post(path, payload);
        context.setResponse(response);
    }

    @Then("all items in the response list should have {string} between {string} and {string}")
    public void allItemsInResponseListShouldHaveDateBetween(String field, String fromDate, String toDate) {
        List<Map<String, Object>> items = context.getResponse().jsonPath().getList("$");
        Assert.assertNotNull(items);
        for (Map<String, Object> item : items) {
            String date = (String) item.get(field);
            Assert.assertNotNull("Field '" + field + "' is null in item", date);
            Assert.assertTrue(
                "Date " + date + " is not between " + fromDate + " and " + toDate,
                date.compareTo(fromDate) >= 0 && date.compareTo(toDate) <= 0
            );
        }
    }

    private Map<String, Object> buildApplicationPayload(Map<String, String> data) {
        Map<String, Object> payload = new HashMap<>();
        for (Map.Entry<String, String> entry : data.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if ("volumeM3".equals(key)) {
                payload.put(key, Double.parseDouble(value));
            } else {
                payload.put(key, value);
            }
        }
        return payload;
    }
}
