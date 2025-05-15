package config;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import java.util.HashMap;
import java.util.Map;

public class Config {

    {
        Map<String, String> headers = new HashMap<>();
        headers.put("app-id", "680fb2afd632d761ae21c249");
        headers.put("Content-Type", "application/json");
        headers.put("Accept", "application/json");

        RequestSpecification requestSpecification = new RequestSpecBuilder()
                .setRelaxedHTTPSValidation()
                .setBaseUri("https://dummyapi.io/data")
                .setBasePath("/v1")
                .addHeaders(headers)
                .addFilter(new RequestLoggingFilter())
                .addFilter(new ResponseLoggingFilter())
                .build();

        ResponseSpecification responseSpecification = new ResponseSpecBuilder()
                .expectContentType("application/json")
                //.expectStatusCode(200)
                .build();

        RestAssured.requestSpecification = requestSpecification;
        RestAssured.responseSpecification = responseSpecification;
    }

}
