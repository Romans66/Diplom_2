package clients.user.delete;

import clients.user.baseClient;
import clients.user.obj.UserRequestObj;
import io.restassured.response.ValidatableResponse;

import static clients.user.UserApi.USER;
import static io.restassured.RestAssured.given;

public class ClientDelete extends baseClient {
    public ValidatableResponse deleteUser(String accessToken, UserRequestObj userObj) {
        String authorizationHeader = "Authorization";
        ValidatableResponse validatableResponse =
                given()
                        .spec(requestSpec)
                        .header(authorizationHeader, accessToken)
                        .body(userObj)
                        .delete(USER)
                        .then();
        return validatableResponse;
    }
}