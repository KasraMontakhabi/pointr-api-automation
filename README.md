# Pointr API Automation

API test automation framework for the Pointr Indoor Intelligence platform, covering the Site, Building, and Level management APIs.

---

## Stack

| Concern | Choice |
|---|---|
| Language | Java 17 |
| Test runner | TestNG 7 |
| HTTP client | REST-assured 5 |
| API mocking | WireMock 3 |
| Serialization | Jackson 2 |
| Reporting | TestNG built-in HTML |
| CI | GitHub Actions |

---

## Project Structure

```
src/test/java/com/pointr/
├── api/              # One class per resource (SiteApi, BuildingApi, LevelApi)
│   └── BaseApi.java  # Shared RequestSpecification
├── models/           # POJOs — Site, Building, Level
├── tests/            # TestNG test classes
│   └── BaseTest.java # WireMock lifecycle + RestAssured base config
└── utils/
    ├── ConfigManager.java   # Environment-aware config loader
    └── TestDataLoader.java  # Loads JSON fixtures from testdata/

src/test/resources/
├── config/
│   ├── dev.properties      # Default environment (localhost:8080)
│   └── staging.properties
├── testdata/               # Request body fixtures per resource
│   ├── site/
│   ├── building/
│   └── level/
├── wiremock/
│   ├── mappings/           # Stub definitions (request matching + response)
│   └── __files/            # Response body files referenced by stubs
└── testng.xml
```

---

## Design Decisions

**API layer abstraction** — Each resource has a dedicated API class (`SiteApi`, `BuildingApi`, `LevelApi`) that extends `BaseApi`. Tests call typed methods (`siteApi.createSite(body)`) rather than raw REST-assured DSL, keeping test code readable and the HTTP layer in one place.

**WireMock over a live server** — All tests run against a WireMock server started in `BaseTest`. This makes the suite fast, deterministic, and runnable with no external dependencies. Stubs are loaded from `wiremock/mappings/` at startup via `usingFilesUnderClasspath("wiremock")`.

**File-based test data** — Request bodies live in `testdata/` JSON files, not inline strings. This separates data from logic, makes edge cases easy to spot, and allows non-engineers to update payloads without touching test code.

**Environment config** — `ConfigManager` reads from `config/{env}.properties` where `env` defaults to `dev`. The framework is structured to support multiple environments via `-Denv=staging`. In this project, all tests run against a local WireMock instance, so the environment flag does not change the test target — it demonstrates the pattern. In a real deployment, each environment config would point to an actual host, and WireMock would be scoped to dev/CI only.

---

## How WireMock Works Here

WireMock acts as a fake HTTP server that runs locally during tests. Instead of calling a real Pointr API, every test sends requests to WireMock, and WireMock replies with pre-defined responses.

**The flow for each test:**

```
Test code
  → sends HTTP request (e.g. POST /sites)
    → WireMock receives it
      → matches it against a stub in wiremock/mappings/
        → returns the configured response (status code + body)
          → test asserts on that response
```

**Stubs** are JSON files in `wiremock/mappings/`. Each stub says: "if you receive a request matching X, respond with Y." For example, a stub might say: if the request is `POST /sites` with a valid body, return `201` with a site object; if the body is missing a required field, return `400`.

**Response bodies** for stubs live in `wiremock/__files/` and are referenced by name from the mapping files, keeping the stub definitions short.

When a test starts, `BaseTest` boots a WireMock server and loads all stubs from the `wiremock/` folder. When the test finishes, the server shuts down. Each test run starts with a clean, predictable state.

---

## Running Tests

**Prerequisites:** Java 17, Maven 3.8+

```bash
# Run all tests (dev environment)
mvn test

# Re-run only failed tests from the last run
mvn test -Dsurefire.suiteXmlFiles=target/surefire-reports/testng-failed.xml
```

---

## Test Reports

Reports are generated to `target/surefire-reports/` after every run:

| File | Content |
|---|---|
| `index.html` | Full suite report with per-test details |
| `emailable-report.html` | Single-page summary, suitable for sharing |
| `testng-failed.xml` | TestNG suite file to re-run failures only |

---

## Test Coverage

| Resource | Tests | Scenarios covered |
|---|---|---|
| Site | 11 | Create, retrieve, delete — happy path, missing field, not found, duplicate, response schema |
| Building | 13 | Create, retrieve, delete — happy path, missing name, missing siteId, invalid siteId, not found, duplicate, response schema |
| Level | 10 | Single import, bulk import, missing buildingId, empty array, missing level name, invalid buildingId, response counts and indexes |
| **Total** | **34** | |

---

## CI

Tests run automatically on every push and pull request to `main` and `develop` via GitHub Actions. The full HTML report is uploaded as a build artifact on every run (pass or fail).

To view reports from a CI run: **Actions → select run → Artifacts → download `surefire-reports`**.
