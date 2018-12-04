package com.juniormiqueletti.moneyapp.resource;

import io.restassured.http.Header;
import io.restassured.response.Response;
import org.junit.Test;
import org.springframework.http.HttpStatus;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.springframework.http.HttpStatus.NOT_FOUND;

public class CategoryRSIT extends AuthenticableTest {

    private static final String CONTENT_TYPE_HEADER = "Content-Type";
    private static final String APPLICATION_JSON = "application/json";
    private static final int UNAUTHORIZED_HTTP_STATUS_CODE = 401;
    private static final int OK_HTTP_STATUS_CODE = 200;
    private static final String CATEGORY_URL = "/api/v1/category";

    @Test
    public void getCategoryWithoutToken() {

        Response response =
            given()
                .header("Accept", APPLICATION_JSON)
                .header(CONTENT_TYPE_HEADER, APPLICATION_JSON)
            .get(CATEGORY_URL)
                .thenReturn();

        String errorDescription = response.getBody().jsonPath().getString("error_description");

        assertEquals(UNAUTHORIZED_HTTP_STATUS_CODE, response.getStatusCode());
        assertEquals(errorDescription, "Full authentication is required to access this resource");
    }

    @Test
    public void getAExistedCategory_shouldReturnOk() {

        Header authenticationHeader = getAuthenticationHeader();

        Response response =
            given()
                .header("Accept", APPLICATION_JSON)
                .header(CONTENT_TYPE_HEADER, APPLICATION_JSON)
                .header(authenticationHeader)
            .get(CATEGORY_URL + "/1")
                .thenReturn();

        assertEquals(OK_HTTP_STATUS_CODE, response.getStatusCode());
    }

    @Test
    public void getACategoryNotFound_shouldReturnAStatusNotFound() {

        Header authenticationHeader = getAuthenticationHeader();

        Response response =
            given()
                .header("Accept", APPLICATION_JSON)
                .header(CONTENT_TYPE_HEADER, APPLICATION_JSON)
                .header(authenticationHeader)
            .get(CATEGORY_URL + "/99999")
                .thenReturn();

        assertEquals(NOT_FOUND.value(), response.getStatusCode());
    }

    @Test
    public void getAllCategories_shouldReturnStatusOk() {
        Header authenticationHeader = getAuthenticationHeader();

        Response response =
            given()
                .header("Accept", APPLICATION_JSON)
                .header(CONTENT_TYPE_HEADER, APPLICATION_JSON)
                .header(authenticationHeader)
            .get(CATEGORY_URL)
                .thenReturn();

        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
    }
}
