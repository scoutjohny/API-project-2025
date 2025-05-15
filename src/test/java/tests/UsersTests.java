package tests;

import config.Config;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import utils.Constants;
import static utils.Constants.*;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class UsersTests extends Config {

    String userId;

    @Test(priority = 1)
    public void getUsersTest(){

        Map<String,Integer> map = new HashMap<>();
        map.put("page",0);
        map.put("limit",10);

        Response response = given()
                .queryParams(map)
                .when().get(GET_ALL_USERS);

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
                .pathParam("id",this.userId)
                .when().get(GET_USER_BY_ID);

    }

    @Test(priority = 3)
    public void createUserTest(){

        String body = "{\n" +
                "    \"title\": \"ms\",\n" +
                "    \"firstName\": \"Sara\",\n" +
                "    \"lastName\": \"Andersen\",\n" +
                "    \"picture\": \"https://randomuser.me/api/portraits/women/58.jpg\",\n" +
                "    \"gender\": \"female\",\n" +
                "    \"email\": \"sara.andersen068877945615890@example.com\",\n" +
                "    \"dateOfBirth\": \"1996-04-30T19:26:49.610Z\",\n" +
                "    \"phone\": \"92694011\",\n" +
                "    \"location\": {\n" +
                "        \"street\": \"9614, SÃ¸ndermarksvej\",\n" +
                "        \"city\": \"Kongsvinger\",\n" +
                "        \"state\": \"Nordjylland\",\n" +
                "        \"country\": \"Denmark\",\n" +
                "        \"timezone\": \"-9:00\"\n" +
                "    }\n" +
                "}";

        Response response = given()
                .body(body)
                .when().post(CREATE_USER);

        this.userId = response.jsonPath().get("id");

    }

    @Test(priority = 4)
    public void updateUserTest(){

        String body = "{\n" +
                "    \"title\": \"ms\",\n" +
                "    \"firstName\": \"Sarita\",\n" +
                "    \"lastName\": \"Andersen\",\n" +
                "    \"picture\": \"https://randomuser.me/api/portraits/women/58.jpg\",\n" +
                "    \"gender\": \"female\",\n" +
                "    \"email\": \"sara.andersen068877945615890@example.com\",\n" +
                "    \"dateOfBirth\": \"1996-04-30T19:26:49.610Z\",\n" +
                "    \"phone\": \"92694011\",\n" +
                "    \"location\": {\n" +
                "        \"street\": \"9614, SÃ¸ndermarksvej\",\n" +
                "        \"city\": \"Kongsvinger\",\n" +
                "        \"state\": \"Nordjylland\",\n" +
                "        \"country\": \"Denmark\",\n" +
                "        \"timezone\": \"-9:00\"\n" +
                "    }\n" +
                "}";

        Response response = given()
                .pathParam("id",this.userId)
                .body(body)
                .when().put(UPDATE_USER);
    }

    @Test(priority = 5)
    public void deleteUserTest(){

        Response response = given()
                .pathParam("id",this.userId)
                .when().delete(DELETE_USER);

        Assert.assertEquals(response.getStatusCode(),200,"Expected 200 but got: " + response.getStatusCode());
        String id = response.jsonPath().get("id");
        System.out.println(id);

        Assert.assertEquals(id, this.userId);

        Response response1 = given()
                .pathParam("id",this.userId)
                .when().delete(DELETE_USER);
        Assert.assertEquals(response1.getStatusCode(),404,"Expected 404 but got: " + response1.getStatusCode());

    }

}
