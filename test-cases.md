# Pointr API Test Cases

## Overview

This document lists all test cases for the Pointr CMS API. Each test case maps directly
to an automated test in the project. Test cases are grouped by resource and category.

**Test Environment:** Local (WireMock on port 8080)  
**Base URL:** `http://localhost:8080/api/v1`

---

## TC Naming Convention

```
TC-[RESOURCE]-[NUMBER] — [Description]
```

Example: `TC-SITE-001 — Successfully create a new site`

---

## 1. Site API Test Cases

### Happy Path

| ID | Test Case | Method | Endpoint | Expected Status |
|---|---|---|---|---|
| TC-SITE-001 | Successfully create a new site | POST | /api/v1/sites | 201 |
| TC-SITE-002 | Successfully retrieve an existing site by ID | GET | /api/v1/sites/{id} | 200 |
| TC-SITE-003 | Successfully delete an existing site | DELETE | /api/v1/sites/{id} | 204 |

### Negative Cases

| ID | Test Case | Method | Endpoint | Expected Status |
|---|---|---|---|---|
| TC-SITE-004 | Create site with missing required field `name` | POST | /api/v1/sites | 400 |
| TC-SITE-005 | Retrieve a site with a non-existent ID | GET | /api/v1/sites/{id} | 404 |
| TC-SITE-006 | Delete a site with a non-existent ID | DELETE | /api/v1/sites/{id} | 404 |
| TC-SITE-007 | Create a site with a duplicate name | POST | /api/v1/sites | 409 |

### Response Validation

| ID | Test Case | Validates |
|---|---|---|
| TC-SITE-008 | Create site response contains generated `id` | `id` is not null |
| TC-SITE-009 | Create site response contains `createdAt` timestamp | `createdAt` is not null |
| TC-SITE-010 | Create site response matches request body fields | `name`, `address` match input |
| TC-SITE-011 | Retrieve site response body matches expected schema | All fields present |

---

## 2. Building API Test Cases

### Happy Path

| ID | Test Case | Method | Endpoint | Expected Status |
|---|---|---|---|---|
| TC-BLDG-001 | Successfully create a new building | POST | /api/v1/buildings | 201 |
| TC-BLDG-002 | Successfully retrieve an existing building by ID | GET | /api/v1/buildings/{id} | 200 |
| TC-BLDG-003 | Successfully delete an existing building | DELETE | /api/v1/buildings/{id} | 204 |

### Negative Cases

| ID | Test Case | Method | Endpoint | Expected Status |
|---|---|---|---|---|
| TC-BLDG-004 | Create building with missing required field `name` | POST | /api/v1/buildings | 400 |
| TC-BLDG-005 | Create building with missing required field `siteId` | POST | /api/v1/buildings | 400 |
| TC-BLDG-006 | Create building with a non-existent `siteId` | POST | /api/v1/buildings | 404 |
| TC-BLDG-007 | Retrieve a building with a non-existent ID | GET | /api/v1/buildings/{id} | 404 |
| TC-BLDG-008 | Delete a building with a non-existent ID | DELETE | /api/v1/buildings/{id} | 404 |
| TC-BLDG-009 | Create a building with a duplicate name in same site | POST | /api/v1/buildings | 409 |

### Response Validation

| ID | Test Case | Validates |
|---|---|---|
| TC-BLDG-010 | Create building response contains generated `id` | `id` is not null |
| TC-BLDG-011 | Create building response contains `siteId` | `siteId` matches request |
| TC-BLDG-012 | Create building response contains `createdAt` timestamp | `createdAt` is not null |
| TC-BLDG-013 | Retrieve building response body matches expected schema | All fields present |

---

## 3. Level API Test Cases

### Happy Path

| ID | Test Case | Method | Endpoint | Expected Status |
|---|---|---|---|---|
| TC-LVL-001 | Successfully import a single level | POST | /api/v1/levels | 201 |
| TC-LVL-002 | Successfully import multiple levels in one request | POST | /api/v1/levels | 201 |

### Negative Cases

| ID | Test Case | Method | Endpoint | Expected Status |
|---|---|---|---|---|
| TC-LVL-003 | Import level with missing required field `buildingId` | POST | /api/v1/levels | 400 |
| TC-LVL-004 | Import level with empty `levels` array | POST | /api/v1/levels | 400 |
| TC-LVL-005 | Import level with missing required field `name` | POST | /api/v1/levels | 400 |
| TC-LVL-006 | Import level with a non-existent `buildingId` | POST | /api/v1/levels | 404 |

### Response Validation

| ID | Test Case | Validates |
|---|---|---|
| TC-LVL-007 | Single import response contains 1 level in array | `imported` equals 1 |
| TC-LVL-008 | Bulk import response contains correct count | `imported` matches input count |
| TC-LVL-009 | Each level in response contains generated `id` | All `id` fields are not null |
| TC-LVL-010 | Each level in response contains `levelIndex` | `levelIndex` matches input |

---

## Test Coverage Summary

| Resource | Happy Path | Negative | Response Validation | Total |
|---|---|---|---|---|
| Site | 3 | 4 | 4 | 11 |
| Building | 3 | 6 | 4 | 13 |
| Level | 2 | 4 | 4 | 10 |
| **Total** | **8** | **14** | **12** | **34** |

---

## Traceability Matrix

Each test case maps to an automated test method:

| Test Case ID | Java Method | Class |
|---|---|---|
| TC-SITE-001 | `shouldCreateSiteSuccessfully()` | SiteApiTest |
| TC-SITE-002 | `shouldRetrieveSiteById()` | SiteApiTest |
| TC-SITE-003 | `shouldDeleteSiteSuccessfully()` | SiteApiTest |
| TC-SITE-004 | `shouldReturn400WhenNameIsMissing()` | SiteApiTest |
| TC-SITE-005 | `shouldReturn404WhenSiteNotFound()` | SiteApiTest |
| TC-SITE-006 | `shouldReturn404WhenDeletingNonExistentSite()` | SiteApiTest |
| TC-SITE-007 | `shouldReturn409WhenSiteNameIsDuplicate()` | SiteApiTest |
| TC-SITE-008 | `shouldReturnGeneratedIdOnCreate()` | SiteApiTest |
| TC-SITE-009 | `shouldReturnCreatedAtOnCreate()` | SiteApiTest |
| TC-SITE-010 | `shouldReturnMatchingFieldsOnCreate()` | SiteApiTest |
| TC-SITE-011 | `shouldReturnValidSchemaOnRetrieve()` | SiteApiTest |
| TC-BLDG-001 | `shouldCreateBuildingSuccessfully()` | BuildingApiTest |
| TC-BLDG-002 | `shouldRetrieveBuildingById()` | BuildingApiTest |
| TC-BLDG-003 | `shouldDeleteBuildingSuccessfully()` | BuildingApiTest |
| TC-BLDG-004 | `shouldReturn400WhenNameIsMissing()` | BuildingApiTest |
| TC-BLDG-005 | `shouldReturn400WhenSiteIdIsMissing()` | BuildingApiTest |
| TC-BLDG-006 | `shouldReturn404WhenSiteIdDoesNotExist()` | BuildingApiTest |
| TC-BLDG-007 | `shouldReturn404WhenBuildingNotFound()` | BuildingApiTest |
| TC-BLDG-008 | `shouldReturn404WhenDeletingNonExistentBuilding()` | BuildingApiTest |
| TC-BLDG-009 | `shouldReturn409WhenBuildingNameIsDuplicate()` | BuildingApiTest |
| TC-BLDG-010 | `shouldReturnGeneratedIdOnCreate()` | BuildingApiTest |
| TC-BLDG-011 | `shouldReturnSiteIdOnCreate()` | BuildingApiTest |
| TC-BLDG-012 | `shouldReturnCreatedAtOnCreate()` | BuildingApiTest |
| TC-BLDG-013 | `shouldReturnValidSchemaOnRetrieve()` | BuildingApiTest |
| TC-LVL-001 | `shouldImportSingleLevelSuccessfully()` | LevelApiTest |
| TC-LVL-002 | `shouldImportMultipleLevelsSuccessfully()` | LevelApiTest |
| TC-LVL-003 | `shouldReturn400WhenBuildingIdIsMissing()` | LevelApiTest |
| TC-LVL-004 | `shouldReturn400WhenLevelsArrayIsEmpty()` | LevelApiTest |
| TC-LVL-005 | `shouldReturn400WhenLevelNameIsMissing()` | LevelApiTest |
| TC-LVL-006 | `shouldReturn404WhenBuildingIdDoesNotExist()` | LevelApiTest |
| TC-LVL-007 | `shouldReturnImportedCountOfOneForSingleLevel()` | LevelApiTest |
| TC-LVL-008 | `shouldReturnCorrectImportedCountForBulkLevels()` | LevelApiTest |
| TC-LVL-009 | `shouldReturnGeneratedIdForEachLevel()` | LevelApiTest |
| TC-LVL-010 | `shouldReturnCorrectLevelIndexForEachLevel()` | LevelApiTest |