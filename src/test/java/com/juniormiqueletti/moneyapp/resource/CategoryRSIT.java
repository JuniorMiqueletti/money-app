package com.juniormiqueletti.moneyapp.resource;

import com.juniormiqueletti.moneyapp.model.Category;
import io.restassured.http.Header;
import io.restassured.response.Response;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.http.HttpStatus.*;

public class CategoryRSIT extends AuthenticableTest {

    private static final String CONTENT_TYPE_HEADER = "Content-Type";
    private static final String APPLICATION_JSON = "application/json";
    private static final String CATEGORY_URL = "/api/v1/category";
    private static final String LOCATION_HEADER = "location";
    private static final String ID_REGEX = "[0-9]+";
    private static final String LOCATION_PATTERN = ".*" + CATEGORY_URL +  "/" + ID_REGEX;

    @Test
    public void getCategoryWithoutToken() {

        Response response =
            given()
                .header("Accept", APPLICATION_JSON)
                .header(CONTENT_TYPE_HEADER, APPLICATION_JSON)
            .get(CATEGORY_URL)
                .thenReturn();

        String errorDescription = response.getBody().jsonPath().getString("error_description");

        assertEquals(UNAUTHORIZED.value(), response.getStatusCode());
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

        assertEquals(OK.value(), response.getStatusCode());
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

        assertEquals(OK.value(), response.getStatusCode());
    }

    @Test
    public void createACategory_shouldReturnOkWithLocationHeader() {
        Header authenticationHeader = getAuthenticationHeader();

        Category category2Create = new Category();
        category2Create.setName("Financial");

        Response response =
            given()
                .body(category2Create)
                .header("Accept", APPLICATION_JSON)
                .header(CONTENT_TYPE_HEADER, APPLICATION_JSON)
                .header(authenticationHeader)
            .when()
                .post(CATEGORY_URL)
            .thenReturn();

        assertEquals(CREATED.value(), response.getStatusCode());
        String location = response.getHeader(LOCATION_HEADER);

        assertTrue(location.matches(LOCATION_PATTERN));
    }
}
