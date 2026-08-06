Feature: Greeting Update Management
  As an API consumer
  I want to update existing greetings and manage greeting templates
  So that I can customize and maintain greeting messages

  Background:
    Given the Greeting API is available at base URL
    And a greeting exists with name "TestUser" and id stored as "existingGreetingId"

  Scenario: Update an existing greeting name
    Given I have an update request body:
      """
      {
        "name": "UpdatedUser",
        "template": "Hello, {name}!"
      }
      """
    When I send a PUT request to "/api/greet/{existingGreetingId}"
    Then the response status code should be 200
    And the response JSON field "name" should equal "UpdatedUser"
    And the response JSON field "message" should equal "Hello, UpdatedUser!"

  Scenario: Update greeting template
    Given I have an update request body:
      """
      {
        "template": "Welcome, {name}! Have a great day."
      }
      """
    When I send a PATCH request to "/api/greet/{existingGreetingId}"
    Then the response status code should be 200
    And the response JSON field "message" should contain "Welcome"
    And the response JSON field "message" should contain "TestUser"

  Scenario: Delete a greeting
    When I send a DELETE request to "/api/greet/{existingGreetingId}"
    Then the response status code should be 204
    When I send a GET request to "/api/greet/{existingGreetingId}"
    Then the response status code should be 404

  Scenario: Update non-existent greeting returns 404
    Given I have an update request body:
      """
      {
        "name": "Nobody"
      }
      """
    When I send a PUT request to "/api/greet/non-existent-id-99999"
    Then the response status code should be 404
    And the response JSON field "error" should not be empty

  Scenario: Update greeting with invalid data returns 400
    Given I have an update request body:
      """
      {
        "name": "",
        "template": ""
      }
      """
    When I send a PUT request to "/api/greet/{existingGreetingId}"
    Then the response status code should be 400
    And the response JSON field "error" should contain "validation"

  Scenario: Batch update greetings
    Given I have a batch update request body:
      """
      {
        "ids": ["{existingGreetingId}"],
        "template": "Hey there, {name}!"
      }
      """
    When I send a POST request to "/api/greet/batch-update"
    Then the response status code should be 200
    And the response JSON field "updatedCount" should equal "1"

  Scenario Outline: Update greeting with different templates
    Given a greeting exists with name "<name>" and id stored as "tempGreetingId"
    And I have an update request body:
      """
      {
        "template": "<template>"
      }
      """
    When I send a PATCH request to "/api/greet/{tempGreetingId}"
    Then the response status code should be 200
    And the response JSON field "message" should contain "<name>"

    Examples:
      | name    | template                            |
      | Alice   | Hi, {name}! Good morning.           |
      | Bob     | Hey {name}, how are you?            |
      | Charlie | Salutations, dear {name}!           |

  Scenario: Update greeting preserves creation timestamp
    When I send a GET request to "/api/greet/{existingGreetingId}"
    Then the response status code should be 200
    And I store the response JSON field "createdAt" as "originalCreatedAt"
    Given I have an update request body:
      """
      {
        "name": "UpdatedTimestampTest"
      }
      """
    When I send a PUT request to "/api/greet/{existingGreetingId}"
    Then the response status code should be 200
    And the response JSON field "createdAt" should equal the stored value "originalCreatedAt"
    And the response JSON field "updatedAt" should not equal the stored value "originalCreatedAt"
