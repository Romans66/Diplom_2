package clients.order.getusersorders;

import clients.user.baseClient;
import io.restassured.response.ValidatableResponse;

import static clients.order.OrderApi.ORDERS;
import static io.restassured.RestAssured.given;

public class ClientGetUsersOrder extends baseClient {
    
    public ValidatableResponse getUsersOrder(String accessToken) {
        String authorizationHeader = "Authorization";
        ValidatableResponse validatableResponse =
                given()
                        .spec(requestSpec)
                        .header(authorizationHeader, accessToken)
                        .get(ORDERS)
                        .then();
        
        return validatableResponse;
    }
    
}