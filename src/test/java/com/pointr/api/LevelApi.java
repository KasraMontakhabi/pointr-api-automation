package com.pointr.api;

import com.pointr.utils.ConfigManager;
import io.restassured.response.Response;

public class LevelApi extends BaseApi {

    private final String endpoint = ConfigManager.getLevelsEndpoint();

    public Response importLevels(String requestBody) {
        return getBaseRequest()
            .body(requestBody)
            .when()
            .post(endpoint);
    }
}