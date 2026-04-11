package com.pointr.tests;

import com.pointr.api.BuildingApi;
import com.pointr.utils.TestDataLoader;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.*;

public class BuildingApiTest extends BaseTest {

    private BuildingApi buildingApi;

    @BeforeClass
    public void setup() {
        buildingApi = new BuildingApi();
    }

    @Test(description = "TC-BLDG-001 - Successfully create a new building")
    public void shouldCreateBuildingSuccessfully() {
        String body = TestDataLoader.load("testdata/building/valid_building.json");

        buildingApi.createBuilding(body)
            .then()
            .statusCode(201)
            .body("id", notNullValue())
            .body("siteId", equalTo("site-001"))
            .body("name", equalTo("Main Hospital Block"));
    }

    @Test(description = "TC-BLDG-002 - Successfully retrieve an existing building by ID")
    public void shouldRetrieveBuildingById() {
        buildingApi.getBuilding("building-001")
            .then()
            .statusCode(200)
            .body("id", equalTo("building-001"))
            .body("siteId", notNullValue())
            .body("name", notNullValue())
            .body("createdAt", notNullValue());
    }

    @Test(description = "TC-BLDG-003 - Successfully delete an existing building")
    public void shouldDeleteBuildingSuccessfully() {
        buildingApi.deleteBuilding("building-001")
            .then()
            .statusCode(204);
    }

    @Test(description = "TC-BLDG-004 - Create building with missing required field name")
    public void shouldReturn400WhenNameIsMissing() {
        String body = TestDataLoader.load("testdata/building/invalid_building_no_name.json");

        buildingApi.createBuilding(body)
            .then()
            .statusCode(400)
            .body("error", equalTo("Field 'name' is required"));
    }

    @Test(description = "TC-BLDG-005 - Create building with missing required field siteId")
    public void shouldReturn400WhenSiteIdIsMissing() {
        String body = TestDataLoader.load("testdata/building/invalid_building_no_siteid.json");

        buildingApi.createBuilding(body)
            .then()
            .statusCode(400)
            .body("error", equalTo("Field 'siteId' is required"));
    }

    @Test(description = "TC-BLDG-006 - Create building with a non-existent siteId")
    public void shouldReturn404WhenSiteIdDoesNotExist() {
        String body = TestDataLoader.load("testdata/building/invalid_building_bad_siteid.json");

        buildingApi.createBuilding(body)
            .then()
            .statusCode(404)
            .body("error", equalTo("Site not found"));
    }

    @Test(description = "TC-BLDG-007 - Retrieve a building with a non-existent ID")
    public void shouldReturn404WhenBuildingNotFound() {
        buildingApi.getBuilding("nonexistent-id")
            .then()
            .statusCode(404)
            .body("error", equalTo("Building not found"));
    }

    @Test(description = "TC-BLDG-008 - Delete a building with a non-existent ID")
    public void shouldReturn404WhenDeletingNonExistentBuilding() {
        buildingApi.deleteBuilding("nonexistent-id")
            .then()
            .statusCode(404)
            .body("error", equalTo("Building not found"));
    }

    @Test(description = "TC-BLDG-009 - Create a building with a duplicate name in same site")
    public void shouldReturn409WhenBuildingNameIsDuplicate() {
        String body = TestDataLoader.load("testdata/building/duplicate_building.json");

        buildingApi.createBuilding(body)
            .then()
            .statusCode(409)
            .body("error", equalTo("A building with this name already exists in this site"));
    }

    @Test(description = "TC-BLDG-010 - Create building response contains generated id")
    public void shouldReturnGeneratedIdOnCreate() {
        String body = TestDataLoader.load("testdata/building/valid_building.json");

        buildingApi.createBuilding(body)
            .then()
            .statusCode(201)
            .body("id", notNullValue())
            .body("id", not(emptyString()));
    }

    @Test(description = "TC-BLDG-011 - Create building response contains siteId")
    public void shouldReturnSiteIdOnCreate() {
        String body = TestDataLoader.load("testdata/building/valid_building.json");

        buildingApi.createBuilding(body)
            .then()
            .statusCode(201)
            .body("siteId", equalTo("site-001"));
    }

    @Test(description = "TC-BLDG-012 - Create building response contains createdAt timestamp")
    public void shouldReturnCreatedAtOnCreate() {
        String body = TestDataLoader.load("testdata/building/valid_building.json");

        buildingApi.createBuilding(body)
            .then()
            .statusCode(201)
            .body("createdAt", notNullValue())
            .body("createdAt", not(emptyString()));
    }

    @Test(description = "TC-BLDG-013 - Retrieve building response body matches expected schema")
    public void shouldReturnValidSchemaOnRetrieve() {
        buildingApi.getBuilding("building-001")
            .then()
            .statusCode(200)
            .body("id", notNullValue())
            .body("siteId", notNullValue())
            .body("name", notNullValue())
            .body("description", notNullValue())
            .body("floorCount", notNullValue())
            .body("createdAt", notNullValue());
    }
}