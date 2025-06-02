package tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import config.Config;
import io.restassured.response.Response;
import listeners.RetryAnalizer;
import listeners.TestListeners;
import model.UserModel.UserRequest;
import model.UserModel.UserResponse;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import service.UserService;
import utils.Utils;

import static utils.Constants.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

@Listeners(TestListeners.class)
public class UsersTests extends Config {

    String userId;
    SoftAssert softAssert;

    @BeforeMethod(alwaysRun = true)
    public void setup() {
        softAssert = new SoftAssert();
    }

    @Test(priority = 1, retryAnalyzer = RetryAnalizer.class)
    public void getUsersTest() {

        Map<String, Integer> map = new HashMap<>();
        map.put("page", 0);
        map.put("limit", 50);

        UserRequest userRequest = UserRequest.createUser();

        UserResponse userResponse = given()
                .body(userRequest)
                .when().post(CREATE_USER).getBody().as(UserResponse.class);

        List<UserResponse> response = given()
                .queryParams(map)
                .when().get(GET_ALL_USERS).jsonPath().getList("data", UserResponse.class);

//        this.userId = response.jsonPath().get("data[0].id");
        String expectedId = userResponse.getId();

        boolean isInTheList = false;
        for (int i = 0; i < response.size(); i++) {
            if(response.get(i).getId().equals(expectedId)){
                isInTheList = true;
            }
            Assert.assertTrue(isInTheList);
        }

//        String actualFirstName = response.jsonPath().get("data[0].firstName");
//        softAssert.assertEquals(response.getStatusCode(), 200, "Expected 200 but got: " + response.getStatusCode());
//        softAssert.assertEquals(actualFirstName, "Roberto");
//        softAssert.assertAll();
//        Assert.assertEquals(response.getStatusCode(),200, "Expected 200 but got: " + response.getStatusCode());
//        String actualFirstName = response.jsonPath().get("data[0].firstName");
//        System.out.println(actualFirstName);
//
//        Assert.assertEquals(actualFirstName,"Sara");
    }

    @Test(priority = 2, retryAnalyzer = RetryAnalizer.class)
    public void getUserByIdTest() {

        Response response = given()
                .pathParam("id", this.userId)
                .when().get(GET_USER_BY_ID);

    }

    @Test(priority = 3)
    public void createUserTest() {

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
    public void createUserUsingJavaObjectTest() {

        UserRequest userRequest = UserRequest.createUser();

        UserResponse response = given()
                .body(userRequest)
                .when().post(CREATE_USER).getBody().as(UserResponse.class);

        System.out.println(response.getId());
        System.out.println(response.getUpdatedDate());
        this.userId = response.getId();

    }

    @Test(priority = 5)
    public void updateUserTest() {

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
                .pathParam("id", this.userId)
                .body(body)
                .when().put(UPDATE_USER);
    }

    @Test(priority = 6)
    public void updateUserTestUsingJavaObject() {

        UserRequest userRequest = UserRequest.createUser();

        UserResponse response = given()
                .body(userRequest)
                .when().post(CREATE_USER).getBody().as(UserResponse.class);

        String updatedFirstName = "updatedFirstName";
        String updatedEmail = "updatedEmail";
        String updatedCity = "updatedCity";

        UserRequest updateUser = userRequest
                .withFirst_name(updatedFirstName)
                .withEmail(updatedEmail)
                .withLocation(userRequest.getLocation().withCity(updatedCity));

        String userId = response.getId();

        //Ovde hvatamo response kao običan response a ne kao UserResponse objekat jer ćemo to odraditi kasnije
        Response updatedUserResponse = given()
                .body(updateUser)
                .pathParam("id", userId)
                .when().put(UPDATE_USER); //.getBody().as(UserResponse.class);

        //Ovde hvatamo status code jer Jackson ne može to sam da radi, on se bavi samo JSON-om!!
        int statusCode = updatedUserResponse.getStatusCode();

        //Ovde definišemo novu promenljivu tipa UserResponse kako bi od response-a napravili objekat tipa UserResponse
        UserResponse updatedUserResponse1 = null;
        try {
            //sad pravimo objekat
            ObjectMapper objectMapper = new ObjectMapper();
            //novi objekat čita sadržaj response-a kao stringove i smešta ih u svoja polja
            updatedUserResponse1 = objectMapper.readValue(updatedUserResponse.getBody().asString(), UserResponse.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        boolean isFirstNameUpdated = updatedUserResponse1.getFirstName().equals(updatedFirstName);

        //provera status koda:
        softAssert.assertEquals(statusCode, 200, "Expected 200 but got: " + statusCode);
        softAssert.assertTrue(isFirstNameUpdated, "First name not updated!");
        softAssert.assertEquals(updatedUserResponse1.getLocation().getCity(), updatedCity, "City not updated!");
        softAssert.assertAll();
    }

    @Test(priority = 7)
    public void deleteUserTest() {

        Response response = given()
                .pathParam("id", this.userId)
                .when().delete(DELETE_USER);

        Assert.assertEquals(response.getStatusCode(), 200, "Expected 200 but got: " + response.getStatusCode());
        String id = response.jsonPath().get("id");
        System.out.println(id);

        Assert.assertEquals(id, this.userId);

        Response response1 = given()
                .pathParam("id", this.userId)
                .when().delete(DELETE_USER);
        Assert.assertEquals(response1.getStatusCode(), 404, "Expected 404 but got: " + response1.getStatusCode());

    }

    @Test
    public void readFromJson() {
        UserResponse userResponse = Utils.getUserFromJson("userRequest");
        System.out.println(userResponse);
    }

}
