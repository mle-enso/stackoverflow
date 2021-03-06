package de.mle.stackoverflow;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import lombok.AccessLevel;
import lombok.Getter;

public class RestAssuredTest {

    @Getter(AccessLevel.NONE)
    int a;

    @Test
    @Disabled
    public void getSpreadSheet() {
        RestAssured.baseURI =
                "https://st8.tuiprjuat.co.uk/searchpanel/departureairport/th" +
                        "?duration=7114&multiSelect=&when=17-04-2019&flexible=true" +
                        "&flexibleDays=3&preventCache=1555326084549";

        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .when()
                .get()
                .then()
                .statusCode(403);
    }
}
