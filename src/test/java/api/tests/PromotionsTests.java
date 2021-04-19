package api.tests;

import api.requests.Promotions;
import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matchers;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import java.util.ArrayList;
import java.util.Map;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class PromotionsTests {

    Response responseForValidAPIKey;
    Response responseForInvalidAPIKey;
    Promotions promotionsRequests;
    String validAPIKey;
    String invalidAPIKey;
    ArrayList<Map<String,?>> jsonObjectArrayList;

    @BeforeClass
    public void setup() {
        promotionsRequests = new Promotions();
        validAPIKey = "GDMSTGExy0sVDlZMzNDdUyZ";
        invalidAPIKey = "GDMSTGExy0sVDlZM";
        responseForValidAPIKey = promotionsRequests.getPromotionsResponse(validAPIKey);
        responseForInvalidAPIKey = promotionsRequests.getPromotionsResponse(invalidAPIKey);
        jsonObjectArrayList = responseForValidAPIKey.jsonPath().get("promotions");
    }

    @Test(priority = 1, groups = {"promotions_tests"})
    @Description("Verifying status code of API response with Valid Key")
    public void testValidResponseStatusCode(){
        assertThat(200, is(responseForValidAPIKey.getStatusCode()));
    }

    @Test(priority = 2, groups = {"promotions_tests"})
    @Description("Verifying 'promotions' key is present in response code")
    public void testJSONKeyPromotions(){
        responseForValidAPIKey.then().body("$", hasKey("promotions"));
    }

    @Test(priority = 3, groups = {"promotions_tests"})
    @Description("Verifying few jsonKey are present in response")
    public void testJSONKeys(){
        for(Map<String, ?> jsonObject: jsonObjectArrayList) {
            assertThat(jsonObject, hasKey("orderId"));
            assertThat(jsonObject, hasKey("promoArea"));
            assertThat(jsonObject, hasKey("promoType"));
            assertThat(jsonObject, hasKey("showPrice"));
            assertThat(jsonObject, hasKey("showText"));
            assertThat(jsonObject, hasKey("localizedTexts"));
        }
    }

    @Test(priority = 4, groups = {"promotions_tests"})
    @Description("Verifying showPrice / showText values")
    public void testJSONBooleanValues(){
        for(Map<String, ?> jsonObject: jsonObjectArrayList) {
            assertThat((Boolean) jsonObject.get("showPrice"), CoreMatchers.anyOf(is(true), is(false)));
            assertThat((Boolean) jsonObject.get("showText"), CoreMatchers.anyOf(is(true), is(false)));
        }
    }

    @Test(priority = 5, groups = {"promotions_tests"})
    @Description("Verifying localizedTexts has both en / ar keys")
    public void testLocalizedTextsKey(){
        for(Map<String, ?> jsonObject: jsonObjectArrayList) {
            assertThat((Map<String, ?>) jsonObject.get("localizedTexts"), CoreMatchers.allOf(hasKey("en"), hasKey("ar")));
        }
    }

    @Test(priority = 6, groups = {"promotions_tests"})
    @Description("Verifying promotionId contains any String value")
    public void testPromotionIdValueInstance(){
        for(Map<String, ?> jsonObject: jsonObjectArrayList) {
            assertThat((String) jsonObject.get("promotionId"), Matchers.<String>instanceOf(String.class));
        }
    }

    @Test(priority = 7, groups = {"promotions_tests"})
    @Description("Verifying programType values")
    public void testProgramTypeValue(){
        for(Map<String, ?> jsonObject: jsonObjectArrayList) {
            ArrayList<Map<String, ?>> propertiesJsonObjectList = (ArrayList<Map<String, ?>>) jsonObject.get("properties");
            for(Map<String, ?> propertiesMap: propertiesJsonObjectList) {
                if(propertiesMap.containsKey("programType")) {
                    assertThat((String) propertiesMap.get("programType"), CoreMatchers.anyOf(
                            equalToIgnoringCase("EPISODE"), equalToIgnoringCase("MOVIE"),
                            equalToIgnoringCase("SERIES"), equalToIgnoringCase("SEASON")));
                }
            }
        }
    }

    @Test(priority = 8, groups = {"promotions_tests"})
    @Description("Verifying status code of API response with invalid Key")
    public void testResponseStatusCodeWithInvalidKey(){
        assertThat(403, is(responseForInvalidAPIKey.getStatusCode()));
    }

    @Test(priority = 9, groups = {"promotions_tests"})
    @Description("Verifying code of API response with invalid Key")
    public void testCodeForInvalidKey(){
        assertThat(8001, is (responseForInvalidAPIKey.jsonPath().getInt("error.code")));
    }

    @Test(priority = 10, groups = {"promotions_tests"})
    @Description("Verifying message of API response with invalid Key")
    public void testMessageForInvalidKey(){
        assertThat("invalid api key", is (responseForInvalidAPIKey.jsonPath().getString("error.message")));
    }

    @Test(priority = 11, groups = {"promotions_tests"})
    @Description("Verifying request id is not null")
    public void testRequestIDNotNull(){
        assertThat(responseForInvalidAPIKey.jsonPath().getString("error.requestId"), notNullValue());
    }


}
