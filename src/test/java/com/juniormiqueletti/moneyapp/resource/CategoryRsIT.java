package com.juniormiqueletti.moneyapp.resource;

import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.response.Response;
import org.junit.Test;

import java.util.Base64;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class CategoryRsIT {

    private static final String URL_OAUTH = "/oauth/token";
    private static final String CONTENT_TYPE_HEADER = "Content-Type";
    private static final String APPLICATION_JSON = "application/json";
    private static final int OK_HTTP_STATUS_CODE = 200;
    private static final int UNAUTHORIZED_HTTP_STATUS_CODE = 401;
    private static final int BAD_REQUEST_HTTP_STATUS_CODE = 400;
    private static final String CATEGORY_URL = "/category";

    private int portNumber = 8080;

    @Test
    public void requestCategoryWithoutToken() {

        Response response =
            RestAssured
                .given()
                .port(portNumber)
                .header("Accept", APPLICATION_JSON)
                .header(CONTENT_TYPE_HEADER, APPLICATION_JSON)
                .get(CATEGORY_URL)
                .thenReturn();

        String errorDescription = response.getBody().jsonPath().getString("error_description");

        assertEquals(UNAUTHORIZED_HTTP_STATUS_CODE, response.getStatusCode());
        assertEquals(errorDescription, "Full authentication is required to access this resource");
    }

    private Response requestToken(final String username, final String password) {

        String clientBasicAuthCredentials =
            Base64.getEncoder().encodeToString("angular:@ngul@r".getBytes());

        Response response =
            RestAssured.given()
                .port(portNumber)
                .header(new Header("Authorization", "Basic " + clientBasicAuthCredentials))
                .queryParam("username", username)
                .queryParam("password", password)
                .queryParam("grant_type", "password")
                .when()
                .post(URL_OAUTH)
                .then()
                    .extract().response();

        return response;
    }
}
