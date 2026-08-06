Feature: Hello World Greeting API
  As an API consumer
  I want to interact with the HelloWorld Greeting Service
  So that I can retrieve personalized greetings

  Background:
    Given the Greeting API is available at base URL

  Scenario: Get default greeting
    When I send a GET request to "/api/greet"
    Then the response status code should be 200
    And the response body should contain "Hello, World!"
    And the response content type should be "application/json"

  Scenario: Get personalized greeting by name
    When I send a GET request to "/api/greet/Java"
    Then the response status code should be 200
    And the response JSON field "message" should equal "Hello, Java!"
    And the response JSON field "name" should equal "Java"

  Scenario: Get greeting with special characters in name
    When I send a GET request to "/api/greet/John%20Doe"
    Then the response status code should be 200
    And the response JSON field "message" should contain "John Doe"

  Scenario: Create a new greeting template
    Given I have a greeting request body:
      """
      {
        "name": "Alice",
        "template": "Greetings, {name}!"
      }
      """
    When I send a POST request to "/api/greet"
    Then the response status code should be 201
    And the response JSON field "message" should equal "Greetings, Alice!"
    And the response JSON field "id" should not be empty

  Scenario: Get greeting returns 404 for empty name
    When I send a GET request to "/api/greet/"
    Then the response status code should be 404

  Scenario Outline: Get greeting for multiple names
    When I send a GET request to "/api/greet/<name>"
    Then the response status code should be 200
    And the response JSON field "message" should equal "Hello, <name>!"

    Examples:
      | name    |
      | Alice   |
      | Bob     |
      | Charlie |
      | World   |

  Scenario: Get all greetings list
    Given greetings exist in the system
    When I send a GET request to "/api/greet/all"
    Then the response status code should be 200
    And the response body should be a JSON array
    And the response JSON array should have at least 1 element

  Scenario: API health check
    When I send a GET request to "/api/health"
    Then the response status code should be 200
    And the response JSON field "status" should equal "UP"
