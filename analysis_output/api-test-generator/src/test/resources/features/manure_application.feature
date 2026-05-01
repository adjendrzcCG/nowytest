@manure @application
Feature: Manure Application Management
  As a farm manager
  I want to record and manage manure applications to fields
  So that I can track nutrient inputs and comply with regulations

  Background:
    Given the Manure API is available
    And I am authenticated as a farm manager

  @smoke
  Scenario: Record a new manure application event
    Given a storage facility with ID "SF001" exists
    And a field with ID "FIELD001" exists
    And I have valid application details
      | field            | value      |
      | storageId        | SF001      |
      | fieldId          | FIELD001   |
      | applicationDate  | 2024-04-01 |
      | volumeM3         | 25.5       |
      | applicationMethod| TRAILING_SHOE |
      | cropType         | WINTER_WHEAT  |
    When I send a POST request to "/api/v1/applications"
    Then the response status code should be 201
    And the response body should contain field "id"
    And the response body should contain field "status" with value "RECORDED"

  Scenario: Get application details
    Given an application with ID "APP001" exists
    When I send a GET request to "/api/v1/applications/APP001"
    Then the response status code should be 200
    And the response body should contain field "id" with value "APP001"
    And the response body should contain field "volumeM3"
    And the response body should contain field "applicationDate"

  Scenario: List all applications for a specific field
    Given field "FIELD001" has 2 recorded applications
    When I send a GET request to "/api/v1/applications?fieldId=FIELD001"
    Then the response status code should be 200
    And the response body should be a list with at least 2 items

  Scenario: List all applications within a date range
    Given there are applications recorded in March 2024
    When I send a GET request to "/api/v1/applications?fromDate=2024-03-01&toDate=2024-03-31"
    Then the response status code should be 200
    And all items in the response list should have "applicationDate" between "2024-03-01" and "2024-03-31"

  Scenario: Update application details before approval
    Given an application with ID "APP001" exists with status "DRAFT"
    And I have updated application details
      | field    | value |
      | volumeM3 | 30.0  |
    When I send a PUT request to "/api/v1/applications/APP001"
    Then the response status code should be 200
    And the response body should contain field "volumeM3" with value "30.0"

  Scenario: Approve a manure application
    Given an application with ID "APP001" exists with status "DRAFT"
    When I send a POST request to "/api/v1/applications/APP001/approve"
    Then the response status code should be 200
    And the response body should contain field "status" with value "APPROVED"
    And the response body should contain field "approvedAt"

  Scenario: Cannot apply manure exceeding field maximum allowance
    Given a field with ID "FIELD001" has a maximum nitrogen allowance of 170 kg/ha
    And the field has already received applications this year totalling 160 kg/ha
    When I send a POST request to "/api/v1/applications" with volume exceeding allowance
    Then the response status code should be 422
    And the response body should contain field "error" with value "NITROGEN_LIMIT_EXCEEDED"

  Scenario: Cannot apply manure during closed period (closed season)
    Given the current date is in a closed application period
    When I send a POST request to "/api/v1/applications" with a closed season date
    Then the response status code should be 422
    And the response body should contain field "error" with value "CLOSED_SEASON_VIOLATION"

  Scenario: Calculate nutrient value of application
    Given an application with ID "APP001" exists
    When I send a GET request to "/api/v1/applications/APP001/nutrients"
    Then the response status code should be 200
    And the response body should contain field "totalNitrogenKg"
    And the response body should contain field "totalPhosphorusKg"
    And the response body should contain field "totalPotassiumKg"

  Scenario Outline: Validate application methods
    Given I have valid application details with method "<method>"
    When I send a POST request to "/api/v1/applications"
    Then the response status code should be 201
    And the response body should contain field "applicationMethod" with value "<method>"

    Examples:
      | method              |
      | TRAILING_SHOE       |
      | BAND_SPREADING      |
      | BROADCAST           |
      | INJECTION           |
