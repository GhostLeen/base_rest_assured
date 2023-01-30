package com.denal.helpers;

import io.qameta.allure.restassured.AllureRestAssured;

public class CustomApiListener {

    private static final AllureRestAssured FILTER = new AllureRestAssured();

    public static AllureRestAssured allureWithCustomTemplates() {
        FILTER.setRequestTemplate("request.ftl");
        FILTER.setResponseTemplate("response.ftl");
        return FILTER;
    }
}