package com.pointr.api;

import com.pointr.utils.ConfigManager;
import io.restassured.response.Response;

public class SiteApi extends BaseApi {

    private final String endpoint = ConfigManager.getSitesEndpoint();

    public Response createSite(String requestBody) {
        return getBaseRequest()
            .body(requestBody)
            .when()
            .post(endpoint);
    }

    public Response getSite(String siteId) {
        return getBaseRequest()
            .when()
            .get(endpoint + "/" + siteId);
    }

    public Response deleteSite(String siteId) {
        return getBaseRequest()
            .when()
            .delete(endpoint + "/" + siteId);
    }
}