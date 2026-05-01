# HelloWorld Greeting API – Cucumber BDD Test Suite

End-to-end API tests for the **HelloWorld Greeting Service** using
[Cucumber](https://cucumber.io/) (BDD / Gherkin) and
[RestAssured](https://rest-assured.io/).

---

## Project Structure

```
api-test-generator/
├── pom.xml                                      Maven build descriptor
├── README.md                                    This file
└── src/
    └── test/
        ├── java/
        │   └── com/example/
        │       ├── runner/
        │       │   └── CucumberRunner.java       JUnit test runner
        │       ├── steps/
        │       │   ├── HelloWorldSteps.java      Core greeting step definitions
        │       │   └── GreetingUpdateSteps.java  Update-specific step definitions
        │       └── support/
        │           ├── ApiClient.java            RestAssured HTTP wrapper
        │           ├── Hooks.java                Before/After Cucumber hooks
        │           ├── TestContext.java          Shared scenario state (DI)
        │           └── TestDataBuilder.java      Fluent JSON request builders
        └── resources/
            ├── cucumber.properties              Cucumber runtime configuration
            └── features/
                ├── hello_world_greeting.feature  Core greeting scenarios
                └── greeting_update.feature       Update / delete scenarios
```

---

## Prerequisites

| Tool        | Minimum version |
|-------------|-----------------|
| Java (JDK)  | 8               |
| Maven       | 3.6             |
| Running API | The HelloWorld Greeting Service must be reachable (see below) |

---

## Configuration

The base URL of the API under test is controlled by the **`api.base.url`**
system property.  The default value is `http://localhost:8080`.

You can override it on the command line:

```bash
mvn test -Dapi.base.url=https://staging.example.com
```

---

## Running the Tests

### Run all tests (default base URL)

```bash
mvn test
```

### Run all tests against a specific environment

```bash
mvn test -Dapi.base.url=https://my-api.example.com
```

### Run a single feature file

```bash
mvn test -Dcucumber.features=src/test/resources/features/hello_world_greeting.feature
```

### Run scenarios tagged with a specific tag

```bash
mvn test -Dcucumber.filter.tags="@smoke"
```

### Run and generate HTML report

```bash
mvn verify
```

The HTML report is generated at:
`target/cucumber-reports/cucumber.html`

The JSON report (consumed by the masterthought plugin) is at:
`target/cucumber-reports/cucumber.json`

---

## Feature Files Overview

### `hello_world_greeting.feature`

| Scenario | Description |
|----------|-------------|
| Get default greeting | `GET /api/greet` returns `Hello, World!` |
| Get personalized greeting | `GET /api/greet/{name}` returns greeting for name |
| Get greeting with special characters | URL-encoded name is decoded correctly |
| Create a new greeting template | `POST /api/greet` creates a custom greeting |
| 404 for empty name | `GET /api/greet/` returns 404 |
| Outline – multiple names | Parameterised GET for Alice, Bob, Charlie, World |
| Get all greetings list | `GET /api/greet/all` returns non-empty array |
| API health check | `GET /api/health` returns `{ "status": "UP" }` |

### `greeting_update.feature`

| Scenario | Description |
|----------|-------------|
| Update an existing greeting name | `PUT /api/greet/{id}` replaces name |
| Update greeting template | `PATCH /api/greet/{id}` updates template only |
| Delete a greeting | `DELETE /api/greet/{id}` removes resource |
| 404 for non-existent update | `PUT` on unknown ID returns 404 |
| 400 for invalid data | `PUT` with blank fields returns 400 |
| Batch update greetings | `POST /api/greet/batch-update` updates multiple IDs |
| Outline – different templates | Parameterised PATCH for Alice, Bob, Charlie |
| Preserve creation timestamp | `updatedAt` changes; `createdAt` stays same |

---

## Architecture

```
CucumberRunner (JUnit)
       │
       ├─ Hooks (Before/After) ──► TestContext (scenario state)
       │
       ├─ HelloWorldSteps ────────► ApiClient ──► RestAssured ──► API under test
       │
       └─ GreetingUpdateSteps ───► ApiClient
                                        │
                                   TestDataBuilder (builds JSON bodies)
```

Dependencies are injected between step definition classes and hooks using
**PicoContainer** (included transitively via `cucumber-picocontainer`).

---

## Adding New Tests

1. Create a new `.feature` file in `src/test/resources/features/`.
2. Implement any missing step definitions in `src/test/java/com/example/steps/`.
3. Re-use `TestContext`, `ApiClient`, and `TestDataBuilder` where possible.
4. Annotate scenarios that are not yet ready with `@ignore` to exclude them
   from CI runs.

---

## Troubleshooting

| Symptom | Likely cause | Fix |
|---------|--------------|-----|
| `Connection refused` | API is not running | Start the service before running tests |
| All scenarios fail on Background step | Wrong base URL | Set `api.base.url` correctly |
| `NullPointerException` in steps | Missing `Given` setup step | Ensure Background runs first |
| JSON parsing errors | API returns non-JSON | Check `Content-Type: application/json` header |
