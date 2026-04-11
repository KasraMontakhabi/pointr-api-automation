package com.pointr.api;

import com.pointr.utils.ConfigManager;
import io.restassured.response.Response;

public class BuildingApi extends BaseApi {

    private final String endpoint = ConfigManager.getBuildingsEndpoint();

    public Response createBuilding(String requestBody) {
        return getBaseRequest()
            .body(requestBody)
            .when()
            .post(endpoint);
    }

    public Response getBuilding(String buildingId) {
        return getBaseRequest()
            .when()
            .get(endpoint + "/" + buildingId);
    }

    public Response deleteBuilding(String buildingId) {
        return getBaseRequest()
            .when()
            .delete(endpoint + "/" + buildingId);
    }
}