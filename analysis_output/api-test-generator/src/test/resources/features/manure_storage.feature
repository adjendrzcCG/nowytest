@manure @storage
Feature: Manure Storage Management
  As a farm manager
  I want to manage manure storage facilities
  So that I can track storage capacity and compliance

  Background:
    Given the Manure API is available
    And I am authenticated as a farm manager

  @smoke
  Scenario: Successfully create a new manure storage facility
    Given I have valid storage facility details
      | field          | value            |
      | name           | North Tank       |
      | type           | SLURRY           |
      | capacityM3     | 500              |
      | location       | North Farm Field |
      | constructionDate | 2020-03-15     |
    When I send a POST request to "/api/v1/storage-facilities"
    Then the response status code should be 201
    And the response body should contain field "id"
    And the response body should contain field "name" with value "North Tank"
    And the response body should contain field "type" with value "SLURRY"
    And the response body should contain field "capacityM3" with value "500"

  @smoke
  Scenario: Retrieve an existing storage facility by ID
    Given a storage facility with ID "SF001" exists
    When I send a GET request to "/api/v1/storage-facilities/SF001"
    Then the response status code should be 200
    And the response body should contain field "id" with value "SF001"
    And the response body should contain field "name"

  Scenario: List all storage facilities
    Given there are 3 storage facilities in the system
    When I send a GET request to "/api/v1/storage-facilities"
    Then the response status code should be 200
    And the response body should be a list with at least 3 items

  Scenario: Update a storage facility capacity
    Given a storage facility with ID "SF001" exists
    And I have updated storage capacity details
      | field      | value |
      | capacityM3 | 750   |
    When I send a PUT request to "/api/v1/storage-facilities/SF001"
    Then the response status code should be 200
    And the response body should contain field "capacityM3" with value "750"

  Scenario: Delete a storage facility
    Given a storage facility with ID "SF_DELETE" exists
    When I send a DELETE request to "/api/v1/storage-facilities/SF_DELETE"
    Then the response status code should be 204

  Scenario: Attempt to create a storage facility with missing required fields
    Given I have incomplete storage facility details with missing "name"
    When I send a POST request to "/api/v1/storage-facilities"
    Then the response status code should be 400
    And the response body should contain field "error"

  Scenario: Attempt to get a non-existent storage facility
    When I send a GET request to "/api/v1/storage-facilities/NONEXISTENT"
    Then the response status code should be 404
    And the response body should contain field "message" with value "Storage facility not found"

  Scenario: Check storage facility current fill level
    Given a storage facility with ID "SF001" exists
    When I send a GET request to "/api/v1/storage-facilities/SF001/fill-level"
    Then the response status code should be 200
    And the response body should contain field "currentM3"
    And the response body should contain field "percentageFull"

  Scenario Outline: Create storage facility with various manure types
    Given I have valid storage facility details for type "<manureType>"
    When I send a POST request to "/api/v1/storage-facilities"
    Then the response status code should be 201
    And the response body should contain field "type" with value "<manureType>"

    Examples:
      | manureType  |
      | SLURRY      |
      | SOLID       |
      | DIGESTATE   |
      | COMPOST     |
