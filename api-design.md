# Pointr API Design

## Overview

This document describes the RESTful API design for the Pointr CMS. The API allows exchanging
information about Sites, Buildings, and Levels for large-scale map production.

**Base URL:** `http://localhost:8080/api/v1`  
**Content-Type:** `application/json`  
**API Version:** v1

---

## Data Model

A Site contains multiple Buildings. A Building contains multiple Levels.

```
Site
 └── Building (many per site)
      └── Level (many per building)
```

---

## 1. Site API

### 1.1 Import a New Site

**Endpoint**
```
POST /api/v1/sites
```

**Request Body**
```json
{
  "name": "Pointr Hospital Campus",
  "description": "Main hospital campus with 3 buildings",
  "address": "123 Medical Drive, Istanbul, TR",
  "latitude": 41.0082,
  "longitude": 28.9784
}
```

| Field | Type | Required | Description |
|---|---|---|---|
| name | String | ✅ | Unique name of the site |
| description | String | ❌ | Optional description |
| address | String | ❌ | Physical address |
| latitude | Double | ❌ | GPS latitude |
| longitude | Double | ❌ | GPS longitude |

**Response — 201 Created**
```json
{
  "id": "site-001",
  "name": "Pointr Hospital Campus",
  "description": "Main hospital campus with 3 buildings",
  "address": "123 Medical Drive, Istanbul, TR",
  "latitude": 41.0082,
  "longitude": 28.9784,
  "createdAt": "2026-04-11T10:00:00Z"
}
```

**Error Responses**

| Status | Reason |
|---|---|
| 400 Bad Request | Missing required field `name` |
| 409 Conflict | A site with the same name already exists |

---

### 1.2 Retrieve an Existing Site

**Endpoint**
```
GET /api/v1/sites/{id}
```

**Path Parameter**

| Parameter | Type | Description |
|---|---|---|
| id | String | Unique site ID returned at creation |

**Response — 200 OK**
```json
{
  "id": "site-001",
  "name": "Pointr Hospital Campus",
  "description": "Main hospital campus with 3 buildings",
  "address": "123 Medical Drive, Istanbul, TR",
  "latitude": 41.0082,
  "longitude": 28.9784,
  "createdAt": "2026-04-11T10:00:00Z"
}
```

**Error Responses**

| Status | Reason |
|---|---|
| 404 Not Found | No site found with the given ID |

---

### 1.3 Delete a Site

**Endpoint**
```
DELETE /api/v1/sites/{id}
```

**Path Parameter**

| Parameter | Type | Description |
|---|---|---|
| id | String | Unique site ID to delete |

**Response — 204 No Content**

No response body.

**Error Responses**

| Status | Reason |
|---|---|
| 404 Not Found | No site found with the given ID |

---

## 2. Building API

### 2.1 Import a New Building

**Endpoint**
```
POST /api/v1/buildings
```

**Request Body**
```json
{
  "siteId": "site-001",
  "name": "Main Hospital Block",
  "description": "Primary patient care building",
  "floorCount": 8
}
```

| Field | Type | Required | Description |
|---|---|---|---|
| siteId | String | ✅ | ID of the parent site |
| name | String | ✅ | Unique name of the building |
| description | String | ❌ | Optional description |
| floorCount | Integer | ❌ | Total number of floors |

**Response — 201 Created**
```json
{
  "id": "building-001",
  "siteId": "site-001",
  "name": "Main Hospital Block",
  "description": "Primary patient care building",
  "floorCount": 8,
  "createdAt": "2026-04-11T10:00:00Z"
}
```

**Error Responses**

| Status | Reason |
|---|---|
| 400 Bad Request | Missing required field `name` or `siteId` |
| 404 Not Found | The referenced `siteId` does not exist |
| 409 Conflict | A building with the same name already exists in this site |

---

### 2.2 Retrieve an Existing Building

**Endpoint**
```
GET /api/v1/buildings/{id}
```

**Path Parameter**

| Parameter | Type | Description |
|---|---|---|
| id | String | Unique building ID returned at creation |

**Response — 200 OK**
```json
{
  "id": "building-001",
  "siteId": "site-001",
  "name": "Main Hospital Block",
  "description": "Primary patient care building",
  "floorCount": 8,
  "createdAt": "2026-04-11T10:00:00Z"
}
```

**Error Responses**

| Status | Reason |
|---|---|
| 404 Not Found | No building found with the given ID |

---

### 2.3 Delete a Building

**Endpoint**
```
DELETE /api/v1/buildings/{id}
```

**Path Parameter**

| Parameter | Type | Description |
|---|---|---|
| id | String | Unique building ID to delete |

**Response — 204 No Content**

No response body.

**Error Responses**

| Status | Reason |
|---|---|
| 404 Not Found | No building found with the given ID |

---

## 3. Level API

### 3.1 Import Single or Multiple Levels

**Endpoint**
```
POST /api/v1/levels
```

This single endpoint handles both single and bulk level imports.
The request body accepts an array — pass one item for a single import,
or multiple items for a bulk import.

**Request Body — Single Level**
```json
{
  "buildingId": "building-001",
  "levels": [
    {
      "name": "Ground Floor",
      "levelIndex": 0,
      "description": "Main entrance and reception"
    }
  ]
}
```

**Request Body — Multiple Levels**
```json
{
  "buildingId": "building-001",
  "levels": [
    {
      "name": "Ground Floor",
      "levelIndex": 0,
      "description": "Main entrance and reception"
    },
    {
      "name": "First Floor",
      "levelIndex": 1,
      "description": "Outpatient clinics"
    },
    {
      "name": "Second Floor",
      "levelIndex": 2,
      "description": "Surgical suites"
    }
  ]
}
```

| Field | Type | Required | Description |
|---|---|---|---|
| buildingId | String | ✅ | ID of the parent building |
| levels | Array | ✅ | One or more level objects |
| levels[].name | String | ✅ | Name of the level |
| levels[].levelIndex | Integer | ✅ | Floor number (0 = ground floor) |
| levels[].description | String | ❌ | Optional description |

**Response — 201 Created**
```json
{
  "buildingId": "building-001",
  "imported": 3,
  "levels": [
    {
      "id": "level-001",
      "name": "Ground Floor",
      "levelIndex": 0,
      "description": "Main entrance and reception",
      "createdAt": "2026-04-11T10:00:00Z"
    },
    {
      "id": "level-002",
      "name": "First Floor",
      "levelIndex": 1,
      "description": "Outpatient clinics",
      "createdAt": "2026-04-11T10:00:00Z"
    },
    {
      "id": "level-003",
      "name": "Second Floor",
      "levelIndex": 2,
      "description": "Surgical suites",
      "createdAt": "2026-04-11T10:00:00Z"
    }
  ]
}
```

**Error Responses**

| Status | Reason |
|---|---|
| 400 Bad Request | Missing required field `buildingId` or empty `levels` array |
| 404 Not Found | The referenced `buildingId` does not exist |

---

## Status Code Summary

| Code | Meaning | Used When |
|---|---|---|
| 200 OK | Success | GET requests |
| 201 Created | Resource created | POST requests |
| 204 No Content | Success, no body | DELETE requests |
| 400 Bad Request | Invalid input | Missing required fields |
| 404 Not Found | Resource missing | ID does not exist |
| 409 Conflict | Duplicate resource | Name already exists |