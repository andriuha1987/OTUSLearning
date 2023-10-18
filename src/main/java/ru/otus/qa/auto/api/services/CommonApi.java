package ru.otus.qa.auto.api.services;

import io.restassured.specification.RequestSpecification;

public class CommonApi {
    protected static final String BASE_URI = System.getProperty("api.base.uri", "https://petstore.swagger.io/v2/");
    protected RequestSpecification requestSpecification;
}
