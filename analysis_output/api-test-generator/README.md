# Manure Management API Tests

Comprehensive API test suite for the Manure Management System using **Cucumber BDD** and **RestAssured**.

---

## Table of Contents

- [Overview](#overview)
- [Technology Stack](#technology-stack)
- [Project Structure](#project-structure)
- [Prerequisites](#prerequisites)
- [Configuration](#configuration)
- [Running the Tests](#running-the-tests)
- [Test Features](#test-features)
- [Writing New Tests](#writing-new-tests)

---

## Overview

This test suite validates the Manure Management System REST API end-to-end using the Behaviour-Driven Development (BDD) approach with Gherkin feature files. The tests cover:

- **Manure Storage Management** – CRUD operations on storage facilities, fill levels
- **Manure Application Management** – Recording, approving, and querying application events
- **Nutrient Analysis** – Submitting samples, recording results, generating recommendations
- **Field Management** – Registering fields, tracking nitrogen balance
- **Compliance & Reporting** – NAP compliance checks, annual reports, CSV/PDF exports

---

## Technology Stack

| Library | Version | Purpose |
|---------|---------|---------|
| Cucumber Java | 7.14.0 | BDD framework and Gherkin DSL |
| Cucumber JUnit | 7.14.0 | JUnit integration for Cucumber |
| Cucumber PicoContainer | 7.14.0 | Dependency injection for step definitions |
| RestAssured | 5.3.2 | HTTP client for REST API calls |
| JUnit | 4.13.2 | Test runner |
| Jackson | 2.15.3 | JSON serialization / deserialization |
| Logback | 1.4.11 | Logging |
| Java | 11+ | Runtime |
| Maven | 3.8+ | Build tool |

---

## Project Structure

```
analysis_output/api-test-generator/
├── pom.xml                                  # Maven build file with all dependencies
├── README.md                                # This file
└── src/
    └── test/
        ├── java/
        │   └── com/example/manure/
        │       ├── runner/
        │       │   └── CucumberTestRunner.java      # JUnit entry point
        │       ├── steps/
        │       │   ├── ManureStorageSteps.java       # Storage facility step definitions
        │       │   ├── ManureApplicationSteps.java   # Application event step definitions
        │       │   ├── NutrientAnalysisSteps.java    # Nutrient analysis step definitions
        │       │   ├── FieldManagementSteps.java     # Field management step definitions
        │       │   └── ComplianceReportingSteps.java # Compliance reporting step definitions
        │       └── support/
        │           ├── ApiClient.java               # RestAssured HTTP wrapper
        │           ├── TestContext.java              # Shared state per scenario
        │           ├── TestDataBuilder.java          # Fluent test data builders
        │           └── CucumberHooks.java            # Before/After scenario hooks
        └── resources/
            ├── features/
            │   ├── manure_storage.feature       # Storage facility BDD scenarios
            │   ├── manure_application.feature   # Application event BDD scenarios
            │   ├── nutrient_analysis.feature    # Nutrient analysis BDD scenarios
            │   ├── field_management.feature     # Field management BDD scenarios
            │   └── compliance_reporting.feature # Compliance & reporting BDD scenarios
            ├── cucumber.properties              # Cucumber runtime configuration
            └── api-test.properties              # API endpoint and auth defaults
```

---

## Prerequisites

1. **Java 11+** – `java -version`
2. **Maven 3.8+** – `mvn -version`
3. **Running API server** – The Manure Management API must be accessible

---

## Configuration

### Environment Variable

Set the base URL of the API server before running tests:

```bash
export API_BASE_URL=http://localhost:8080
```

### System Property (Maven)

```bash
mvn test -Dapi.base.url=http://localhost:8080
```

### `api-test.properties`

Edit `src/test/resources/api-test.properties` to change default values:

```properties
api.base.url=http://localhost:8080
api.auth.username=farm_manager
api.auth.password=password123
```

---

## Running the Tests

### Run the full test suite

```bash
cd analysis_output/api-test-generator
mvn test
```

### Run only smoke tests

```bash
mvn test -Dcucumber.filter.tags="@smoke"
# or use the Maven profile
mvn test -Psmoke
```

### Run tests against staging

```bash
mvn test -Pstaging
```

### Run a specific feature

```bash
mvn test -Dcucumber.features="src/test/resources/features/manure_storage.feature"
```

### Run tests by tag

```bash
# Storage tests only
mvn test -Dcucumber.filter.tags="@storage"

# Application tests only
mvn test -Dcucumber.filter.tags="@application"

# Compliance tests only
mvn test -Dcucumber.filter.tags="@compliance"
```

### Generate HTML report

```bash
mvn verify
# Report is generated at: target/cucumber-html-reports/overview-features.html
```

---

## Test Features

### Manure Storage (`@storage`)

| Scenario | Description |
|----------|-------------|
| Create storage facility | POST with valid payload → 201 + id |
| Get facility by ID | GET existing → 200 + data |
| List all facilities | GET list → 200 + at least N items |
| Update capacity | PUT → 200 + updated value |
| Delete facility | DELETE → 204 |
| Missing required field | POST incomplete → 400 |
| Non-existent facility | GET unknown id → 404 |
| Fill level check | GET fill-level → 200 + metrics |
| Different manure types | Parameterised for SLURRY / SOLID / DIGESTATE / COMPOST |

### Manure Application (`@application`)

| Scenario | Description |
|----------|-------------|
| Record application event | POST → 201 + RECORDED status |
| Get application | GET by id → 200 |
| List by field | GET ?fieldId= → 200 |
| Filter by date range | GET ?fromDate&toDate → filtered list |
| Update draft | PUT before approval → 200 |
| Approve application | POST /approve → APPROVED status |
| Nitrogen limit breach | POST over limit → 422 |
| Closed season violation | POST during closed period → 422 |
| Nutrient value calculation | GET /nutrients → 200 + totals |
| Application methods | Parameterised for all methods |

### Nutrient Analysis (`@analysis`)

| Scenario | Description |
|----------|-------------|
| Submit sample | POST → 201 + PENDING |
| Record results | PUT /results → 200 + COMPLETE |
| Get analysis | GET by id → 200 |
| List by storage | GET ?storageId= → list |
| Latest analysis | GET /latest → most recent |
| Recommendations | GET /recommendations → rate guidance |
| Default values | GET /defaults → default table values |

### Field Management (`@fields`)

| Scenario | Description |
|----------|-------------|
| Register field | POST → 201 + id |
| Get field | GET by id → 200 |
| List by farm | GET ?farmId= → list |
| Update crop type | PUT → 200 + updated value |
| Application history | GET /applications → list |
| Nitrogen balance | GET /nitrogen-balance → balance data |
| NVZ allowance check | NVZ field has 170 kg/ha limit |
| Different soil types | Parameterised for all soil types |

### Compliance & Reporting (`@compliance`)

| Scenario | Description |
|----------|-------------|
| Annual report | POST /annual → 202 + reportId |
| Get report status | GET by id → COMPLETE + downloadUrl |
| Nitrogen balance | GET /nitrogen-balance → totals |
| NAP compliance check | GET /nap-check → compliant flag |
| Flag breach | Non-compliant field → compliant=false + breach amount |
| List compliance issues | GET /issues → list |
| Export CSV | GET ?format=CSV → text/csv |
| Export PDF | GET ?format=PDF → application/pdf |
| Closed season dates | GET /closed-season → periods list |

---

## Writing New Tests

### 1. Add a Gherkin scenario

Edit the relevant `.feature` file in `src/test/resources/features/`:

```gherkin
@manure @storage
Scenario: My new scenario
  Given a storage facility with ID "SF001" exists
  When I send a GET request to "/api/v1/storage-facilities/SF001"
  Then the response status code should be 200
```

### 2. Add a step definition (if needed)

If the scenario uses a new step that doesn't exist yet, add it to the appropriate `*Steps.java` class:

```java
@Given("my new step {string}")
public void myNewStep(String param) {
    // implement step logic
}
```

### 3. Use TestDataBuilder for payloads

```java
Map<String, Object> payload = TestDataBuilder.storageFacility()
    .name("My Facility")
    .type("SOLID")
    .capacityM3(300)
    .build();
```

### 4. Access response in assertions

```java
context.getResponse().then()
    .statusCode(200)
    .body("name", equalTo("My Facility"));
```
