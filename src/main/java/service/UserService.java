package service;

import io.restassured.response.Response;
import model.UserModel.UserRequest;

import static io.restassured.RestAssured.given;

public class UserService {

    private static final String GET_ALL_USERS = "user";

    public Response get(){
        UserRequest user = UserRequest.createUser();

        return given()
                .when().post(GET_ALL_USERS);
    }
}
