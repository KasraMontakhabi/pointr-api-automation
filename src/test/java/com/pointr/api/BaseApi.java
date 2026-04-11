package com.pointr.api;

import io.restassured.specification.RequestSpecification;
import static io.restassured.RestAssured.given;

public class BaseApi {

    // Every request in the project starts from here
    protected RequestSpecification getBaseRequest() {
        return given()
            .contentType("application/json")
            .accept("application/json");
    }
}