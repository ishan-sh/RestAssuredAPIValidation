package api.requests;

import io.restassured.RestAssured;
import static io.restassured.RestAssured.*;
import io.restassured.response.Response;


public class Promotions {

    public Promotions() {
        RestAssured.baseURI = "http://api.intigral-ott.net/";
    }

    public Response getPromotionsResponse(String apiKeyValue) {
        return given()
                .param("apikey", apiKeyValue)
                .get("popcorn-api-rs-7.9.10/v1/promotions")
                .then()
                .extract()
                .response();
    }

}
