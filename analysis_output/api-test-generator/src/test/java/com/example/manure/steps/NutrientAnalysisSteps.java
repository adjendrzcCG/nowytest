package com.example.manure.steps;

import com.example.manure.support.ApiClient;
import com.example.manure.support.TestContext;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.restassured.response.Response;
import org.junit.Assert;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NutrientAnalysisSteps {

    private final TestContext context;
    private final ApiClient apiClient;

    public NutrientAnalysisSteps(TestContext context) {
        this.context = context;
        this.apiClient = context.getApiClient();
    }

    @Given("I have valid sample details")
    public void iHaveValidSampleDetails(DataTable dataTable) {
        Map<String, String> data = dataTable.asMap(String.class, String.class);
        Map<String, Object> payload = new HashMap<>(data);
        context.setRequestBody(payload);
    }

    @Given("a nutrient analysis with ID {string} exists")
    public void aNutrientAnalysisWithIDExists(String analysisId) {
        Response response = apiClient.get("/api/v1/nutrient-analyses/" + analysisId);
        if (response.getStatusCode() == 404) {
            Map<String, Object> payload = new HashMap<>();
            payload.put("id", analysisId);
            payload.put("storageId", "SF001");
            payload.put("sampleDate", "2024-03-01");
            payload.put("sampleType", "SLURRY");
            payload.put("labReference", "LAB-2024-" + analysisId);
            Response createResponse = apiClient.post("/api/v1/nutrient-analyses", payload);
            Assert.assertEquals(201, createResponse.getStatusCode());
        }
        context.setCurrentResourceId(analysisId);
    }

    @Given("a nutrient analysis with ID {string} exists with status {string}")
    public void aNutrientAnalysisWithIDExistsWithStatus(String analysisId, String status) {
        aNutrientAnalysisWithIDExists(analysisId);
    }

    @Given("a nutrient analysis with ID {string} with complete results exists")
    public void aNutrientAnalysisWithCompleteResultsExists(String analysisId) {
        aNutrientAnalysisWithIDExists(analysisId);
        Map<String, Object> results = new HashMap<>();
        results.put("totalNitrogenGPerL", 3.2);
        results.put("ammoniumNGPerL", 1.8);
        results.put("phosphorusGPerL", 0.45);
        results.put("potassiumGPerL", 2.1);
        results.put("dryMatterPercent", 6.5);
        Response updateResponse = apiClient.put("/api/v1/nutrient-analyses/" + analysisId + "/results", results);
        Assert.assertEquals(200, updateResponse.getStatusCode());
    }

    @Given("I have nutrient analysis results")
    public void iHaveNutrientAnalysisResults(DataTable dataTable) {
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

    @Given("storage facility {string} has {int} nutrient analyses")
    public void storageFacilityHasNutrientAnalyses(String storageId, int count) {
        for (int i = 0; i < count; i++) {
            Map<String, Object> payload = new HashMap<>();
            payload.put("storageId", storageId);
            payload.put("sampleDate", "2024-0" + (i + 1) + "-15");
            payload.put("sampleType", "SLURRY");
            payload.put("labReference", "LAB-AUTO-" + i);
            apiClient.post("/api/v1/nutrient-analyses", payload);
        }
    }

    @Given("storage facility {string} has multiple nutrient analyses")
    public void storageFacilityHasMultipleNutrientAnalyses(String storageId) {
        storageFacilityHasNutrientAnalyses(storageId, 2);
    }

    @Given("storage facility {string} has no nutrient analyses")
    public void storageFacilityHasNoNutrientAnalyses(String storageId) {
        // Nothing to set up - the storage facility just has no analyses
        context.put("storageId", storageId);
    }

    @Given("a field with ID {string} exists with crop type {string}")
    public void aFieldExistsWithCropType(String fieldId, String cropType) {
        Response response = apiClient.get("/api/v1/fields/" + fieldId);
        if (response.getStatusCode() == 404) {
            Map<String, Object> payload = new HashMap<>();
            payload.put("id", fieldId);
            payload.put("name", "Test Field " + fieldId);
            payload.put("areaHectares", 10.0);
            payload.put("soilType", "CLAY_LOAM");
            payload.put("nitrateZone", "NVZ");
            payload.put("cropType", cropType);
            payload.put("farmId", "FARM001");
            Response createResponse = apiClient.post("/api/v1/fields", payload);
            Assert.assertEquals(201, createResponse.getStatusCode());
        } else {
            Map<String, Object> update = new HashMap<>();
            update.put("cropType", cropType);
            apiClient.put("/api/v1/fields/" + fieldId, update);
        }
    }

    @Then("the response body should contain field {string} with value {string}")
    public void theResponseBodyShouldContainFieldWithValueDouble(String field, String value) {
        String actual = context.getResponse().jsonPath().getString(field);
        Assert.assertEquals("Field '" + field + "' mismatch", value, actual);
    }
}
