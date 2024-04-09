package com.mlieshoff.shaft.integration.cdc;

import com.mlieshoff.shaft.ShaftApplication;
import com.mlieshoff.shaft.integration.ContainerizedIntegrationTestBase;
import com.mlieshoff.shaft.rest.LocationRestController;

import io.restassured.config.EncoderConfig;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.module.mockmvc.config.RestAssuredMockMvcConfig;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = ShaftApplication.class)
public abstract class CdcBaseClass extends ContainerizedIntegrationTestBase {

    @Autowired private LocationRestController unitUnderTest;

    @BeforeEach
    void setUp() {
        EncoderConfig encoderConfig =
                new EncoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false);
        RestAssuredMockMvc.config = new RestAssuredMockMvcConfig().encoderConfig(encoderConfig);
        RestAssuredMockMvc.standaloneSetup(unitUnderTest);
    }
}
