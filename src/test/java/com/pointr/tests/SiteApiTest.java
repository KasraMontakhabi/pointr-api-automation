package com.pointr.tests;

import com.pointr.api.SiteApi;
import com.pointr.models.Site;
import com.pointr.utils.TestDataLoader;
import io.restassured.common.mapper.TypeRef;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
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

        Response response = siteApi.createSite(body);
        response.then().statusCode(201);
        Site site = response.as(new TypeRef<Site>() {});
        assertThat(site.getId(), notNullValue());
        assertThat(site.getName(), equalTo("Pointr Hospital Campus"));
        assertThat(site.getAddress(), equalTo("123 Medical Drive, Istanbul, TR"));
    }

    @Test(description = "TC-SITE-002 - Successfully retrieve an existing site by ID")
    public void shouldRetrieveSiteById() {
        Response response = siteApi.getSite("site-001");
        response.then().statusCode(200);
        Site site = response.as(new TypeRef<Site>() {});
        assertThat(site.getId(), equalTo("site-001"));
        assertThat(site.getName(), notNullValue());
        assertThat(site.getCreatedAt(), notNullValue());
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

        Response response = siteApi.createSite(body);
        response.then().statusCode(201);
        Site site = response.as(new TypeRef<Site>() {});
        assertThat(site.getId(), notNullValue());
        assertThat(site.getId(), not(emptyString()));
    }

    @Test(description = "TC-SITE-009 - Create site response contains createdAt timestamp")
    public void shouldReturnCreatedAtOnCreate() {
        String body = TestDataLoader.load("testdata/site/valid_site.json");

        Response response = siteApi.createSite(body);
        response.then().statusCode(201);
        Site site = response.as(new TypeRef<Site>() {});
        assertThat(site.getCreatedAt(), notNullValue());
        assertThat(site.getCreatedAt(), not(emptyString()));
    }

    @Test(description = "TC-SITE-010 - Create site response matches request body fields")
    public void shouldReturnMatchingFieldsOnCreate() {
        String body = TestDataLoader.load("testdata/site/valid_site.json");

        Response response = siteApi.createSite(body);
        response.then().statusCode(201);
        Site site = response.as(new TypeRef<Site>() {});
        assertThat(site.getName(), equalTo("Pointr Hospital Campus"));
        assertThat(site.getAddress(), equalTo("123 Medical Drive, Istanbul, TR"));
        assertThat(site.getLatitude(), equalTo(41.0082));
        assertThat(site.getLongitude(), equalTo(28.9784));
    }

    @Test(description = "TC-SITE-011 - Retrieve site response body matches expected schema")
    public void shouldReturnValidSchemaOnRetrieve() {
        Response response = siteApi.getSite("site-001");
        response.then().statusCode(200);
        Site site = response.as(new TypeRef<Site>() {});
        assertThat(site.getId(), notNullValue());
        assertThat(site.getName(), notNullValue());
        assertThat(site.getDescription(), notNullValue());
        assertThat(site.getAddress(), notNullValue());
        assertThat(site.getLatitude(), notNullValue());
        assertThat(site.getLongitude(), notNullValue());
        assertThat(site.getCreatedAt(), notNullValue());
    }
}
