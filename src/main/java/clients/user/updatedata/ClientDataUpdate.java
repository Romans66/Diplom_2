package clients.user.updatedata;

import clients.user.baseClient;
import clients.user.obj.UserRequestObj;
import io.restassured.response.ValidatableResponse;

import static clients.user.UserApi.USER;
import static io.restassured.RestAssured.given;

public class ClientDataUpdate extends baseClient {
    public ValidatableResponse dataUpdateUser(String accessToken, UserRequestObj userObj) {
        String authorizationHeader = "Authorization";
        ValidatableResponse validatableResponse =
                given()
                        .spec(requestSpec)
                        .header(authorizationHeader, accessToken)
                        .body(userObj)
                        .patch(USER)
                        .then();
        return validatableResponse;
    }
}