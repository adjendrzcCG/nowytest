@manure @fields
Feature: Farm Field Management
  As a farm manager
  I want to manage farm fields
  So that I can assign manure applications and track nutrient history

  Background:
    Given the Manure API is available
    And I am authenticated as a farm manager

  @smoke
  Scenario: Register a new farm field
    Given I have valid field registration details
      | field        | value         |
      | name         | North Pasture |
      | areaHectares | 12.5          |
      | soilType     | CLAY_LOAM     |
      | nitrateZone  | NVZ           |
      | cropType     | WINTER_WHEAT  |
      | farmId       | FARM001       |
    When I send a POST request to "/api/v1/fields"
    Then the response status code should be 201
    And the response body should contain field "id"
    And the response body should contain field "name" with value "North Pasture"
    And the response body should contain field "areaHectares" with value "12.5"

  Scenario: Get field details by ID
    Given a field with ID "FIELD001" exists
    When I send a GET request to "/api/v1/fields/FIELD001"
    Then the response status code should be 200
    And the response body should contain field "id" with value "FIELD001"
    And the response body should contain field "name"
    And the response body should contain field "areaHectares"

  Scenario: List all fields for a farm
    Given farm "FARM001" has 4 registered fields
    When I send a GET request to "/api/v1/fields?farmId=FARM001"
    Then the response status code should be 200
    And the response body should be a list with at least 4 items

  Scenario: Update field details
    Given a field with ID "FIELD001" exists
    And I have updated field details
      | field    | value        |
      | cropType | SPRING_BARLEY |
    When I send a PUT request to "/api/v1/fields/FIELD001"
    Then the response status code should be 200
    And the response body should contain field "cropType" with value "SPRING_BARLEY"

  Scenario: View application history for a field
    Given a field with ID "FIELD001" has 3 application records
    When I send a GET request to "/api/v1/fields/FIELD001/applications"
    Then the response status code should be 200
    And the response body should be a list with at least 3 items

  Scenario: View nitrogen balance for a field in a given year
    Given a field with ID "FIELD001" has applications for 2023
    When I send a GET request to "/api/v1/fields/FIELD001/nitrogen-balance?year=2023"
    Then the response status code should be 200
    And the response body should contain field "totalAppliedKgPerHa"
    And the response body should contain field "remainingAllowanceKgPerHa"

  Scenario: Field in Nitrate Vulnerable Zone has lower allowance
    Given a field with ID "FIELD_NVZ" exists in a Nitrate Vulnerable Zone
    When I send a GET request to "/api/v1/fields/FIELD_NVZ/nitrogen-balance?year=2024"
    Then the response status code should be 200
    And the response body should contain field "maxAllowedKgPerHa" with value "170"

  Scenario Outline: Register fields with different soil types
    Given I have valid field registration details for soil type "<soilType>"
    When I send a POST request to "/api/v1/fields"
    Then the response status code should be 201
    And the response body should contain field "soilType" with value "<soilType>"

    Examples:
      | soilType    |
      | CLAY_LOAM   |
      | SANDY_LOAM  |
      | PEAT        |
      | CHALK       |
