package tests;

import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class UsersTests {

    String userId;

    @Test(priority = 1)
    public void getUsersTest(){

        Map<String,Integer> map = new HashMap<>();
        map.put("page",0);
        map.put("limit",10);

        Response response = given()
                .baseUri("https://dummyapi.io/data")
                .basePath("/v1")
                .header("app-id","680fb2afd632d761ae21c249")
                .queryParams(map)
                .log().all()
                .when().get("/User");
        //Štampa json response body-ja:
        System.out.println(response.prettyPeek());
        this.userId = response.jsonPath().get("data[0].id");

        SoftAssert softAssert = new SoftAssert();
        String actualFirstName = response.jsonPath().get("data[0].firstName");
        softAssert.assertEquals(response.getStatusCode(),200, "Expected 200 but got: " + response.getStatusCode());
        softAssert.assertEquals(actualFirstName,"Sara");
        softAssert.assertAll();
//        Assert.assertEquals(response.getStatusCode(),200, "Expected 200 but got: " + response.getStatusCode());
//        String actualFirstName = response.jsonPath().get("data[0].firstName");
//        System.out.println(actualFirstName);
//
//        Assert.assertEquals(actualFirstName,"Sara");
    }

    @Test(priority = 2)
    public void getUserByIdTest(){

        Response response = given()
                .baseUri("https://dummyapi.io/data")
                .basePath("/v1")
                .header("app-id","680fb2afd632d761ae21c249")
                .pathParam("id",this.userId)
                .log().all()
                .when().get("/User/{id}");

        //Štampa json response body-ja:
        System.out.println(response.prettyPeek());
    }
}
