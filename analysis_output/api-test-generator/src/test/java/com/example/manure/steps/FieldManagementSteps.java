package com.example.manure.steps;

import com.example.manure.support.ApiClient;
import com.example.manure.support.TestContext;
import io.cucumber.java.en.Given;
import io.restassured.response.Response;
import org.junit.Assert;

import java.util.HashMap;
import java.util.Map;

public class FieldManagementSteps {

    private final TestContext context;
    private final ApiClient apiClient;

    public FieldManagementSteps(TestContext context) {
        this.context = context;
        this.apiClient = context.getApiClient();
    }

    @Given("I have valid field registration details")
    public void iHaveValidFieldRegistrationDetails(io.cucumber.datatable.DataTable dataTable) {
        Map<String, String> data = dataTable.asMap(String.class, String.class);
        Map<String, Object> payload = new HashMap<>();
        for (Map.Entry<String, String> entry : data.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if ("areaHectares".equals(key)) {
                payload.put(key, Double.parseDouble(value));
            } else {
                payload.put(key, value);
            }
        }
        context.setRequestBody(payload);
    }

    @Given("I have valid field registration details for soil type {string}")
    public void iHaveValidFieldRegistrationDetailsForSoilType(String soilType) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("name", "Test Field " + soilType);
        payload.put("areaHectares", 8.5);
        payload.put("soilType", soilType);
        payload.put("nitrateZone", "NON_NVZ");
        payload.put("cropType", "SPRING_BARLEY");
        payload.put("farmId", "FARM001");
        context.setRequestBody(payload);
    }

    @Given("farm {string} has {int} registered fields")
    public void farmHasRegisteredFields(String farmId, int count) {
        for (int i = 0; i < count; i++) {
            Map<String, Object> payload = new HashMap<>();
            payload.put("name", "Auto Field " + i);
            payload.put("areaHectares", 5.0 + i);
            payload.put("soilType", "CLAY_LOAM");
            payload.put("nitrateZone", "NVZ");
            payload.put("cropType", "WINTER_WHEAT");
            payload.put("farmId", farmId);
            apiClient.post("/api/v1/fields", payload);
        }
    }

    @Given("I have updated field details")
    public void iHaveUpdatedFieldDetails(io.cucumber.datatable.DataTable dataTable) {
        Map<String, String> data = dataTable.asMap(String.class, String.class);
        Map<String, Object> payload = new HashMap<>(data);
        context.setRequestBody(payload);
    }

    @Given("a field with ID {string} has {int} application records")
    public void aFieldHasApplicationRecords(String fieldId, int count) {
        for (int i = 0; i < count; i++) {
            Map<String, Object> payload = new HashMap<>();
            payload.put("storageId", "SF001");
            payload.put("fieldId", fieldId);
            payload.put("applicationDate", "2023-0" + (i + 3) + "-15");
            payload.put("volumeM3", 15.0 + i);
            payload.put("applicationMethod", "TRAILING_SHOE");
            payload.put("cropType", "WINTER_WHEAT");
            apiClient.post("/api/v1/applications", payload);
        }
    }

    @Given("a field with ID {string} has applications for {int}")
    public void aFieldHasApplicationsForYear(String fieldId, int year) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("storageId", "SF001");
        payload.put("fieldId", fieldId);
        payload.put("applicationDate", year + "-04-01");
        payload.put("volumeM3", 20.0);
        payload.put("applicationMethod", "BAND_SPREADING");
        payload.put("cropType", "WINTER_WHEAT");
        apiClient.post("/api/v1/applications", payload);
    }

    @Given("a field with ID {string} exists in a Nitrate Vulnerable Zone")
    public void aFieldExistsInNitrateVulnerableZone(String fieldId) {
        Response response = apiClient.get("/api/v1/fields/" + fieldId);
        if (response.getStatusCode() == 404) {
            Map<String, Object> payload = new HashMap<>();
            payload.put("id", fieldId);
            payload.put("name", "NVZ Test Field");
            payload.put("areaHectares", 10.0);
            payload.put("soilType", "CLAY_LOAM");
            payload.put("nitrateZone", "NVZ");
            payload.put("cropType", "WINTER_WHEAT");
            payload.put("farmId", "FARM001");
            Response createResponse = apiClient.post("/api/v1/fields", payload);
            Assert.assertEquals(201, createResponse.getStatusCode());
        }
    }
}
