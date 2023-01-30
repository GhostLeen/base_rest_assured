package com.denal.spec;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static io.restassured.RestAssured.with;
import static io.restassured.filter.log.LogDetail.BODY;
import static io.restassured.filter.log.LogDetail.STATUS;

public class Specs {
    public static RequestSpecification request = with()
            .baseUri("https://reqres.in")
            .basePath("/api")
            .log().all()
            .contentType(ContentType.JSON);

    public static ResponseSpecification response200 = new ResponseSpecBuilder()
            .log(BODY)
            .expectStatusCode(200)
//          .expectBody(containsString("success"))
            .build();

    public static ResponseSpecification response201 = new ResponseSpecBuilder()
            .log(BODY)
            .expectStatusCode(201)
            .build();

    public static ResponseSpecification response400 = new ResponseSpecBuilder()
            .log(STATUS)
            .expectStatusCode(400)
            .build();
}
