package com.example.manure.steps;

import com.example.manure.support.ApiClient;
import com.example.manure.support.TestContext;
import io.cucumber.java.en.Given;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

public class ComplianceReportingSteps {

    private final TestContext context;
    private final ApiClient apiClient;

    public ComplianceReportingSteps(TestContext context) {
        this.context = context;
        this.apiClient = context.getApiClient();
    }

    @Given("the farm has recorded applications for year {int}")
    public void theFarmHasRecordedApplicationsForYear(int year) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("storageId", "SF001");
        payload.put("fieldId", "FIELD001");
        payload.put("applicationDate", year + "-04-01");
        payload.put("volumeM3", 20.0);
        payload.put("applicationMethod", "TRAILING_SHOE");
        payload.put("cropType", "WINTER_WHEAT");
        apiClient.post("/api/v1/applications", payload);
    }

    @Given("a report with ID {string} has been generated")
    public void aReportWithIDHasBeenGenerated(String reportId) {
        Response response = apiClient.get("/api/v1/reports/" + reportId);
        if (response.getStatusCode() == 404) {
            Map<String, Object> payload = new HashMap<>();
            payload.put("year", 2023);
            payload.put("farmId", "FARM001");
            payload.put("reportType", "ANNUAL_MANURE_MANAGEMENT");
            Response createResponse = apiClient.post("/api/v1/reports/annual", payload);
            String newId = createResponse.jsonPath().getString("reportId");
            context.put("currentReportId", newId);
        } else {
            context.put("currentReportId", reportId);
        }
    }

    @Given("the farm {string} has applications and fertiliser purchases for {int}")
    public void theFarmHasApplicationsAndFertiliserPurchasesForYear(String farmId, int year) {
        theFarmHasRecordedApplicationsForYear(year);
        // Record fertiliser purchase
        Map<String, Object> fertiliser = new HashMap<>();
        fertiliser.put("farmId", farmId);
        fertiliser.put("year", year);
        fertiliser.put("productName", "Ammonium Nitrate");
        fertiliser.put("nitrogenContent", 34.5);
        fertiliser.put("quantityKg", 1000.0);
        apiClient.post("/api/v1/fertiliser-purchases", fertiliser);
    }

    @Given("field {string} has had applications recorded for {int}")
    public void fieldHasHadApplicationsRecordedForYear(String fieldId, int year) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("storageId", "SF001");
        payload.put("fieldId", fieldId);
        payload.put("applicationDate", year + "-03-20");
        payload.put("volumeM3", 25.0);
        payload.put("applicationMethod", "TRAILING_SHOE");
        payload.put("cropType", "WINTER_WHEAT");
        apiClient.post("/api/v1/applications", payload);
    }

    @Given("field {string} has exceeded nitrogen limits for {int}")
    public void fieldHasExceededNitrogenLimitsForYear(String fieldId, int year) {
        // Create multiple heavy applications to exceed 170 kg N/ha
        for (int i = 0; i < 5; i++) {
            Map<String, Object> payload = new HashMap<>();
            payload.put("storageId", "SF001");
            payload.put("fieldId", fieldId);
            payload.put("applicationDate", year + "-0" + (i + 2) + "-01");
            payload.put("volumeM3", 100.0); // Large volume to exceed limits
            payload.put("applicationMethod", "BROADCAST");
            payload.put("cropType", "WINTER_WHEAT");
            apiClient.post("/api/v1/applications", payload);
        }
    }

    @Given("the farm {string} has multiple compliance issues")
    public void theFarmHasMultipleComplianceIssues(String farmId) {
        fieldHasExceededNitrogenLimitsForYear("FIELD002", 2023);
    }

    @Given("the farm has applications for year {int}")
    public void theFarmHasApplicationsForYear(int year) {
        theFarmHasRecordedApplicationsForYear(year);
    }
}
