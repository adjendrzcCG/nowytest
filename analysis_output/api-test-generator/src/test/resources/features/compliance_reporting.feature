@manure @compliance @reporting
Feature: Manure Compliance and Reporting
  As a farm manager
  I want to generate compliance reports for manure management
  So that I can meet regulatory requirements and audit obligations

  Background:
    Given the Manure API is available
    And I am authenticated as a farm manager

  @smoke
  Scenario: Generate annual manure management report
    Given the farm has recorded applications for year 2023
    When I send a POST request to "/api/v1/reports/annual" with body
      """
      {
        "year": 2023,
        "farmId": "FARM001",
        "reportType": "ANNUAL_MANURE_MANAGEMENT"
      }
      """
    Then the response status code should be 202
    And the response body should contain field "reportId"
    And the response body should contain field "status" with value "GENERATING"

  Scenario: Get report status and download link
    Given a report with ID "RPT001" has been generated
    When I send a GET request to "/api/v1/reports/RPT001"
    Then the response status code should be 200
    And the response body should contain field "status" with value "COMPLETE"
    And the response body should contain field "downloadUrl"

  Scenario: Check nitrogen balance for the farm
    Given the farm "FARM001" has applications and fertiliser purchases for 2023
    When I send a GET request to "/api/v1/reports/nitrogen-balance?farmId=FARM001&year=2023"
    Then the response status code should be 200
    And the response body should contain field "totalNitrogenAppliedKg"
    And the response body should contain field "totalNitrogenFromManureKg"
    And the response body should contain field "totalNitrogenFromFertiliserKg"
    And the response body should contain field "balanceKg"

  Scenario: Verify compliance with Nitrates Action Programme limits
    Given field "FIELD001" has had applications recorded for 2023
    When I send a GET request to "/api/v1/compliance/nap-check?fieldId=FIELD001&year=2023"
    Then the response status code should be 200
    And the response body should contain field "compliant"
    And the response body should contain field "totalNitrogenAppliedKgPerHa"
    And the response body should contain field "maxAllowedKgPerHa" with value "170"

  Scenario: Flag a compliance breach for a field
    Given field "FIELD002" has exceeded nitrogen limits for 2023
    When I send a GET request to "/api/v1/compliance/nap-check?fieldId=FIELD002&year=2023"
    Then the response status code should be 200
    And the response body should contain field "compliant" with value "false"
    And the response body should contain field "breachKgPerHa"

  Scenario: List all compliance issues for the farm
    Given the farm "FARM001" has multiple compliance issues
    When I send a GET request to "/api/v1/compliance/issues?farmId=FARM001"
    Then the response status code should be 200
    And the response body should be a list with at least 1 items

  Scenario: Export applications to CSV
    Given the farm has applications for year 2023
    When I send a GET request to "/api/v1/reports/export?format=CSV&year=2023&farmId=FARM001"
    Then the response status code should be 200
    And the response content type should be "text/csv"

  Scenario: Export applications to PDF
    Given the farm has applications for year 2023
    When I send a GET request to "/api/v1/reports/export?format=PDF&year=2023&farmId=FARM001"
    Then the response status code should be 200
    And the response content type should be "application/pdf"

  Scenario: Closed season dates check for current year
    When I send a GET request to "/api/v1/compliance/closed-season?year=2024"
    Then the response status code should be 200
    And the response body should contain field "closedPeriods"
    And the response body list "closedPeriods" should not be empty
