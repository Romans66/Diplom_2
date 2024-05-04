package clients.user.login;

import clients.user.baseClient;
import clients.user.obj.UserRequestObj;
import io.restassured.response.ValidatableResponse;

import static clients.user.UserApi.LOGIN;
import static io.restassured.RestAssured.given;

public class ClientLogin extends baseClient {
    public ValidatableResponse login(UserRequestObj userObj) {
        ValidatableResponse validatableResponse =
                given()
                        .spec(requestSpec)
                        .body(userObj)
                        .post(LOGIN)
                        .then();
        return validatableResponse;
    }
}