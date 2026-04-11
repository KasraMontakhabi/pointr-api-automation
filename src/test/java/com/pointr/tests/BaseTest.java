package com.pointr.tests;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.pointr.utils.ConfigManager;
import io.restassured.RestAssured;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

public class BaseTest {

    private static final Logger log = LoggerFactory.getLogger(BaseTest.class);
    private static WireMockServer wireMockServer;

    @BeforeSuite
    public void startMockServer() {

        wireMockServer = new WireMockServer(
            WireMockConfiguration.options()
                .port(ConfigManager.getPort())
                .usingFilesUnderClasspath("wiremock")
        );

        wireMockServer.start();

        RestAssured.baseURI = ConfigManager.getBaseUrl();
        RestAssured.port = ConfigManager.getPort();
        RestAssured.basePath = ConfigManager.getBasePath();

        log.info("WireMock started on port {}", ConfigManager.getPort());
    }

    @AfterSuite
    public void stopMockServer() {
        if (wireMockServer != null && wireMockServer.isRunning()) {
            wireMockServer.stop();
            log.info("WireMock stopped.");
        }
    }
}