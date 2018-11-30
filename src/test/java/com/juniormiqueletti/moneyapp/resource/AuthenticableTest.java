package com.juniormiqueletti.moneyapp.resource;

import io.restassured.http.Header;
import io.restassured.response.Response;

import java.util.Base64;

import static io.restassured.RestAssured.given;

public class AuthenticableTest {

    private static final String URL_OAUTH = "/oauth/token";

    public Header getAuthenticationHeader() {
        Response authenticationResponse = requestToken("admin@gmail.com", "admin");
        String token =  authenticationResponse.getBody().jsonPath().getString("access_token");
        return new Header("Authorization", "bearer " + token);
    }

    private Response requestToken(final String username, final String password) {

        String clientBasicAuthCredentials =
            Base64.getEncoder().encodeToString("angular:@ngul@r".getBytes());

        Response response =
            given()
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
