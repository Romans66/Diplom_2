package clients.user.reg;

import clients.user.UserApi;
import clients.user.baseClient;
import clients.user.obj.UserRequestObj;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class ClientUserReg extends baseClient {
    public ValidatableResponse userAdd(UserRequestObj userObj) {
        ValidatableResponse validatableResponse =
                given()
                        .spec(requestSpec)
                        .body(userObj)
                        .post(UserApi.USER_CREATE)
                        .then();
        return validatableResponse;
    }
}