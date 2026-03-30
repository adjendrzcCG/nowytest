@manure @nutrient @analysis
Feature: Manure Nutrient Analysis
  As a farm manager
  I want to analyse nutrient content of manure
  So that I can optimise fertiliser applications and reduce costs

  Background:
    Given the Manure API is available
    And I am authenticated as a farm manager

  @smoke
  Scenario: Submit manure sample for nutrient analysis
    Given a storage facility with ID "SF001" exists
    And I have valid sample details
      | field        | value      |
      | storageId    | SF001      |
      | sampleDate   | 2024-03-15 |
      | sampleType   | SLURRY     |
      | labReference | LAB-2024-001 |
    When I send a POST request to "/api/v1/nutrient-analyses"
    Then the response status code should be 201
    And the response body should contain field "id"
    And the response body should contain field "status" with value "PENDING"

  Scenario: Record nutrient analysis results
    Given a nutrient analysis with ID "NA001" exists with status "PENDING"
    And I have nutrient analysis results
      | field             | value |
      | totalNitrogenGPerL| 3.2   |
      | ammoniumNGPerL    | 1.8   |
      | phosphorusGPerL   | 0.45  |
      | potassiumGPerL    | 2.1   |
      | dryMatterPercent  | 6.5   |
    When I send a PUT request to "/api/v1/nutrient-analyses/NA001/results"
    Then the response status code should be 200
    And the response body should contain field "status" with value "COMPLETE"
    And the response body should contain field "totalNitrogenGPerL" with value "3.2"

  Scenario: Get nutrient analysis by ID
    Given a nutrient analysis with ID "NA001" exists
    When I send a GET request to "/api/v1/nutrient-analyses/NA001"
    Then the response status code should be 200
    And the response body should contain field "id" with value "NA001"
    And the response body should contain field "sampleDate"

  Scenario: List all nutrient analyses for a storage facility
    Given storage facility "SF001" has 3 nutrient analyses
    When I send a GET request to "/api/v1/nutrient-analyses?storageId=SF001"
    Then the response status code should be 200
    And the response body should be a list with at least 3 items

  Scenario: Get the latest nutrient analysis for a storage facility
    Given storage facility "SF001" has multiple nutrient analyses
    When I send a GET request to "/api/v1/nutrient-analyses/latest?storageId=SF001"
    Then the response status code should be 200
    And the response body should contain field "storageId" with value "SF001"

  Scenario: Calculate application rate recommendations based on nutrient analysis
    Given a nutrient analysis with ID "NA001" with complete results exists
    And a field with ID "FIELD001" exists with crop type "WINTER_WHEAT"
    When I send a GET request to "/api/v1/nutrient-analyses/NA001/recommendations?fieldId=FIELD001"
    Then the response status code should be 200
    And the response body should contain field "recommendedVolumeM3"
    And the response body should contain field "expectedNitrogenKg"
    And the response body should contain field "nitrogenEfficiencyPercent"

  Scenario: Default nutrient values used when no analysis available
    Given storage facility "SF001" has no nutrient analyses
    When I send a GET request to "/api/v1/nutrient-analyses/defaults?manureType=SLURRY"
    Then the response status code should be 200
    And the response body should contain field "totalNitrogenGPerL"
    And the response body should contain field "source" with value "DEFAULT_TABLE"
