package com.pointr.tests;

import com.pointr.api.SiteApi;
import com.pointr.utils.TestDataLoader;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.*;

public class SiteApiTest extends BaseTest {

    private SiteApi siteApi;

    @BeforeClass
    public void setup() {
        siteApi = new SiteApi();
    }

    @Test(description = "TC-SITE-001 - Successfully create a new site")
    public void shouldCreateSiteSuccessfully() {
        String body = TestDataLoader.load("testdata/site/valid_site.json");

        siteApi.createSite(body)
            .then()
            .statusCode(201)
            .body("id", notNullValue())
            .body("name", equalTo("Pointr Hospital Campus"))
            .body("address", equalTo("123 Medical Drive, Istanbul, TR"));
    }

    @Test(description = "TC-SITE-002 - Successfully retrieve an existing site by ID")
    public void shouldRetrieveSiteById() {
        siteApi.getSite("site-001")
            .then()
            .statusCode(200)
            .body("id", equalTo("site-001"))
            .body("name", notNullValue())
            .body("createdAt", notNullValue());
    }

    @Test(description = "TC-SITE-003 - Successfully delete an existing site")
    public void shouldDeleteSiteSuccessfully() {
        siteApi.deleteSite("site-001")
            .then()
            .statusCode(204);
    }

    @Test(description = "TC-SITE-004 - Create site with missing required field name")
    public void shouldReturn400WhenNameIsMissing() {
        String body = TestDataLoader.load("testdata/site/invalid_site.json");

        siteApi.createSite(body)
            .then()
            .statusCode(400)
            .body("error", equalTo("Field 'name' is required"));
    }

    @Test(description = "TC-SITE-005 - Retrieve a site with a non-existent ID")
    public void shouldReturn404WhenSiteNotFound() {
        siteApi.getSite("nonexistent-id")
            .then()
            .statusCode(404)
            .body("error", equalTo("Site not found"));
    }

    @Test(description = "TC-SITE-006 - Delete a site with a non-existent ID")
    public void shouldReturn404WhenDeletingNonExistentSite() {
        siteApi.deleteSite("nonexistent-id")
            .then()
            .statusCode(404)
            .body("error", equalTo("Site not found"));
    }

    @Test(description = "TC-SITE-007 - Create a site with a duplicate name")
    public void shouldReturn409WhenSiteNameIsDuplicate() {
        String body = TestDataLoader.load("testdata/site/duplicate_site.json");

        siteApi.createSite(body)
            .then()
            .statusCode(409)
            .body("error", equalTo("A site with this name already exists"));
    }

    @Test(description = "TC-SITE-008 - Create site response contains generated id")
    public void shouldReturnGeneratedIdOnCreate() {
        String body = TestDataLoader.load("testdata/site/valid_site.json");

        siteApi.createSite(body)
            .then()
            .statusCode(201)
            .body("id", notNullValue())
            .body("id", not(emptyString()));
    }

    @Test(description = "TC-SITE-009 - Create site response contains createdAt timestamp")
    public void shouldReturnCreatedAtOnCreate() {
        String body = TestDataLoader.load("testdata/site/valid_site.json");

        siteApi.createSite(body)
            .then()
            .statusCode(201)
            .body("createdAt", notNullValue())
            .body("createdAt", not(emptyString()));
    }

    @Test(description = "TC-SITE-010 - Create site response matches request body fields")
    public void shouldReturnMatchingFieldsOnCreate() {
        String body = TestDataLoader.load("testdata/site/valid_site.json");

        siteApi.createSite(body)
            .then()
            .statusCode(201)
            .body("name", equalTo("Pointr Hospital Campus"))
            .body("address", equalTo("123 Medical Drive, Istanbul, TR"))
            .body("latitude", equalTo(41.0082f))
            .body("longitude", equalTo(28.9784f));
    }

    @Test(description = "TC-SITE-011 - Retrieve site response body matches expected schema")
    public void shouldReturnValidSchemaOnRetrieve() {
        siteApi.getSite("site-001")
            .then()
            .statusCode(200)
            .body("id", notNullValue())
            .body("name", notNullValue())
            .body("description", notNullValue())
            .body("address", notNullValue())
            .body("latitude", notNullValue())
            .body("longitude", notNullValue())
            .body("createdAt", notNullValue());
    }
}