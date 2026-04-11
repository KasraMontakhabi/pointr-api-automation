package com.pointr.tests;

import com.pointr.api.LevelApi;
import com.pointr.utils.TestDataLoader;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.*;

public class LevelApiTest extends BaseTest {

    private LevelApi levelApi;

    @BeforeClass
    public void setup() {
        levelApi = new LevelApi();
    }

    @Test(description = "TC-LVL-001 - Successfully import a single level")
    public void shouldImportSingleLevelSuccessfully() {
        String body = TestDataLoader.load("testdata/level/single_level.json");

        levelApi.importLevels(body)
            .then()
            .statusCode(201)
            .body("buildingId", equalTo("building-001"))
            .body("imported", equalTo(1))
            .body("levels", hasSize(1));
    }

    @Test(description = "TC-LVL-002 - Successfully import multiple levels in one request")
    public void shouldImportMultipleLevelsSuccessfully() {
        String body = TestDataLoader.load("testdata/level/multiple_levels.json");

        levelApi.importLevels(body)
            .then()
            .statusCode(201)
            .body("buildingId", equalTo("building-001"))
            .body("imported", equalTo(3))
            .body("levels", hasSize(3));
    }

    @Test(description = "TC-LVL-003 - Import level with missing required field buildingId")
    public void shouldReturn400WhenBuildingIdIsMissing() {
        String body = TestDataLoader.load("testdata/level/invalid_level_no_buildingid.json");

        levelApi.importLevels(body)
            .then()
            .statusCode(400)
            .body("error", equalTo("Field 'buildingId' is required"));
    }

    @Test(description = "TC-LVL-004 - Import level with empty levels array")
    public void shouldReturn400WhenLevelsArrayIsEmpty() {
        String body = TestDataLoader.load("testdata/level/invalid_level_empty_array.json");

        levelApi.importLevels(body)
            .then()
            .statusCode(400)
            .body("error", equalTo("Levels array must not be empty"));
    }

    @Test(description = "TC-LVL-005 - Import level with missing required field name")
    public void shouldReturn400WhenLevelNameIsMissing() {
        String body = TestDataLoader.load("testdata/level/invalid_level_no_name.json");

        levelApi.importLevels(body)
            .then()
            .statusCode(400)
            .body("error", equalTo("Field 'name' is required for each level"));
    }

    @Test(description = "TC-LVL-006 - Import level with a non-existent buildingId")
    public void shouldReturn404WhenBuildingIdDoesNotExist() {
        String body = TestDataLoader.load("testdata/level/invalid_level_bad_buildingid.json");

        levelApi.importLevels(body)
            .then()
            .statusCode(404)
            .body("error", equalTo("Building not found"));
    }

    @Test(description = "TC-LVL-007 - Single import response contains imported count of 1")
    public void shouldReturnImportedCountOfOneForSingleLevel() {
        String body = TestDataLoader.load("testdata/level/single_level.json");

        levelApi.importLevels(body)
            .then()
            .statusCode(201)
            .body("imported", equalTo(1));
    }

    @Test(description = "TC-LVL-008 - Bulk import response contains correct imported count")
    public void shouldReturnCorrectImportedCountForBulkLevels() {
        String body = TestDataLoader.load("testdata/level/multiple_levels.json");

        levelApi.importLevels(body)
            .then()
            .statusCode(201)
            .body("imported", equalTo(3));
    }

    @Test(description = "TC-LVL-009 - Each level in response contains generated id")
    public void shouldReturnGeneratedIdForEachLevel() {
        String body = TestDataLoader.load("testdata/level/multiple_levels.json");

        levelApi.importLevels(body)
            .then()
            .statusCode(201)
            .body("levels[0].id", notNullValue())
            .body("levels[1].id", notNullValue())
            .body("levels[2].id", notNullValue());
    }

    @Test(description = "TC-LVL-010 - Each level in response contains correct levelIndex")
    public void shouldReturnCorrectLevelIndexForEachLevel() {
        String body = TestDataLoader.load("testdata/level/multiple_levels.json");

        levelApi.importLevels(body)
            .then()
            .statusCode(201)
            .body("levels[0].levelIndex", equalTo(0))
            .body("levels[1].levelIndex", equalTo(1))
            .body("levels[2].levelIndex", equalTo(2));
    }
}