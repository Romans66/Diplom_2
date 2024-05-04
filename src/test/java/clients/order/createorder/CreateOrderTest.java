package clients.order.createorder;

import clients.order.getIngredients.ClientGetIngredients;
import clients.order.requestobj.IngredientsRequestObj;
import clients.order.responseobj.IngredientsResponseObj;
import clients.user.BaseUserClientTest;
import clients.user.UserFaker;
import clients.user.login.ClientLogin;
import clients.user.obj.UserRequestObj;
import clients.user.reg.ClientUserReg;
import io.qameta.allure.Step;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class CreateOrderTest extends BaseUserClientTest {
    
    // Экземпляры для пользователя
    private UserRequestObj userObj;
    private ClientUserReg clientUser;
    private UserFaker userFaker;
    private ClientLogin clientLogin;
    private String accessToken;
    
    // Экземпляры для заказа
    private ClientCreateOrder clientCreateOrder;
    private IngredientsResponseObj ingredientsResponseObj;
    private IngredientsRequestObj ingredientsRequestObj;
    private ClientGetIngredients clientGetIngredients;
    private Random random;
    private String randomIngredient;
    
    // Поля для тестов
    private final String emptyAccessToken = "";
    private final String success = "success";
    private final String message = "message";
    private final boolean trueValue = true;
    private final boolean falseValue = false;
    
    @Before
    @Step("Подготовка тестовых данных")
    public void setUp() {
        // Создаем тестового пользователя и генерируем данные
        userFaker = new UserFaker();
        userFaker.generateUserData();
        
        clientUser = new ClientUserReg();
        clientLogin = new ClientLogin();
        userObj = new UserRequestObj(userFaker.getEmail(), userFaker.getPassword(), userFaker.getName());
        
        // Создаем и достаем access token тестового пользователя
        validatableResponse = clientUser.userAdd(userObj).statusCode(200);
        accessToken =
                clientLogin.login(userObj).statusCode(200).extract().body().jsonPath().getString("accessToken");
        
        // Достаем ингредиенты по апи
        clientGetIngredients = new ClientGetIngredients();
        ingredientsResponseObj = new IngredientsResponseObj();
        clientCreateOrder = new ClientCreateOrder();
        random = new Random();
        
        clientGetIngredients.getIngredients().statusCode(200);
        ingredientsResponseObj = clientGetIngredients.getIngredientsResponseObj();
        randomIngredient = ingredientsResponseObj.getData()
                .get(random.nextInt(ingredientsResponseObj.getData().size())).get_id();
    }
    
    @Test
    @Step("Проверка оформления заказа под неавторизованным пользователем без ингредиентов")
    public void unauthorizedCreateOrderWithoutIngredientsTest() {
        IngredientsRequestObj emptyingredientsRequestObj = new IngredientsRequestObj();
        String withoutIngredientsMessage = "Ingredient ids must be provided";
        
        clientCreateOrder.createOrder(emptyAccessToken, emptyingredientsRequestObj)
                .statusCode(400)
                .body(success, equalTo(falseValue))
                .body(message, equalTo(withoutIngredientsMessage))
        ;
    }
    
    @Test
    @Step("Проверка оформления заказа под авторизованным пользователем без ингредиентов")
    public void authorizedCreateOrderWithoutIngredientsTest() {
        IngredientsRequestObj emptyingredientsRequestObj = new IngredientsRequestObj();
        String withoutIngredientsMessage = "Ingredient ids must be provided";
        
        clientCreateOrder.createOrder(accessToken, emptyingredientsRequestObj)
                .statusCode(400)
                .body(success, equalTo(falseValue))
                .body(message, equalTo(withoutIngredientsMessage))
        ;
    }
    
    @Test
    @Step("Проверка оформления заказа под неавторизованным пользователем с ингредиентом")
    public void unauthorizedCreateOrderTest() {
        String nameField = "name";
        String orderNumberField = "order.number";
        
        ingredientsRequestObj = new IngredientsRequestObj(randomIngredient);
        
        clientCreateOrder.createOrder(emptyAccessToken, ingredientsRequestObj)
                .statusCode(200)
                .body(success, equalTo(trueValue))
                .body(nameField, notNullValue())
                .body(orderNumberField, notNullValue())
        ;
    }
    
    @Test
    @Step("Проверка оформления заказа под неавторизованным пользователем с ингредиентом с неверным хешем")
    public void unauthorizedCreateOrderWithIncorrectIngredientTest() {
        ingredientsRequestObj = new IngredientsRequestObj(randomIngredient + userFaker.getRandomString());
        
        clientCreateOrder.createOrder(emptyAccessToken, ingredientsRequestObj)
                .statusCode(500);
    }
    
    @Test
    @Step("Проверка оформления заказа под авторизованным пользователем с ингредиентом с неверным хешем")
    public void authorizedCreateOrderWithIncorrectIngredientTest() {
        ingredientsRequestObj = new IngredientsRequestObj(randomIngredient + userFaker.getRandomString());
        
        clientCreateOrder.createOrder(accessToken, ingredientsRequestObj)
                .statusCode(500);
    }
    
    @Test
    @Step("Проверка оформления заказа под авторизованным пользователем с ингредиентом")
    public void authorizedCreateOrderTest() {
        String nameField = "name";
        String orderNumberField = "order.number";
        String orderIngredientsField = "order.ingredients";
        String OrderOwnerField = "order.owner";
        String OrderStatusField = "order.status";
        String OrderNumberField = "order.number";
        String OrderPriceField = "order.price";
        
        ingredientsRequestObj = new IngredientsRequestObj(randomIngredient);
        
        clientCreateOrder.createOrder(accessToken, ingredientsRequestObj)
                .statusCode(200)
                .body(success, equalTo(trueValue))
                .body(nameField, notNullValue())
                .body(orderNumberField, notNullValue())
                .body(orderNumberField, notNullValue())
                .body(orderIngredientsField, notNullValue())
                .body(OrderOwnerField, notNullValue())
                .body(OrderStatusField, notNullValue())
                .body(OrderNumberField, notNullValue())
                .body(OrderPriceField, notNullValue())
        ;
    }
    
    @After
    @Step("Удаление созданного тестового пользователя")
    public void tearDown() {
        deleteUser(userObj);
    }
}