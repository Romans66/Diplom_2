package clients.order.getIngredients;

import clients.order.responseobj.IngredientsResponseObj;
import clients.user.baseClient;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;

import static clients.order.OrderApi.GET_INGREDIENTS;
import static io.restassured.RestAssured.given;

public class ClientGetIngredients extends baseClient {
    private IngredientsResponseObj ingredientsResponseObj;
    
    public IngredientsResponseObj getIngredientsResponseObj() {
        return ingredientsResponseObj;
    }
    
    public ValidatableResponse getIngredients() {
        Response response =
                given()
                        .spec(requestSpec)
                        .get(GET_INGREDIENTS);
        
        // Попытка десериализации
        try {
            ingredientsResponseObj = response.body().as(IngredientsResponseObj.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        ValidatableResponse validatableResponse = response.then();
        
        return validatableResponse;
    }
}