package clients.order.createorder;

import clients.order.requestobj.IngredientsRequestObj;
import clients.user.baseClient;
import io.restassured.response.ValidatableResponse;

import static clients.order.OrderApi.ORDERS;
import static io.restassured.RestAssured.given;

public class ClientCreateOrder extends baseClient {
    
    public ValidatableResponse createOrder(String accessToken, IngredientsRequestObj ingredientsRequestObj) {
        String authorizationHeader = "Authorization";
        ValidatableResponse validatableResponse =
                given()
                        .spec(requestSpec)
                        .header(authorizationHeader, accessToken)
                        .body(ingredientsRequestObj)
                        .post(ORDERS)
                        .then();
        return validatableResponse;
    }
    
}