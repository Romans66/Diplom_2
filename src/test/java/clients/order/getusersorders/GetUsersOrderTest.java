package clients.order.getusersorders;

import clients.order.createorder.ClientCreateOrder;
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

public class GetUsersOrderTest extends BaseUserClientTest {
    private ClientGetUsersOrder clientGetUsersOrder;
    private UserFaker userFaker;
    private UserRequestObj userObj;
    private ClientGetIngredients clientGetIngredients;
    private IngredientsResponseObj ingredientsResponseObj;
    private IngredientsRequestObj ingredientsRequestObj;
    private ClientCreateOrder clientCreateOrder;
    private Random random;
    private String randomIngredient;
    private String accessToken;
    
    // Поля для тестов
    private final String success = "success";
    private final String message = "message";
    private final boolean trueValue = true;
    private final boolean falseValue = false;
    
    @Before
    @Step("Подготовка тестовых данных")
    public void setUp() {
        clientGetUsersOrder = new ClientGetUsersOrder();
        
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
    @Step("Проверка получения заказов неавторизованного пользователя")
    public void unauthorizedGetOrdersTest() {
        String unauthorizedErrorMessage = "You should be authorised";
        String emptyAccessToken = "";
        clientGetUsersOrder.getUsersOrder(emptyAccessToken)
                .statusCode(401)
                .body(success, equalTo(falseValue))
                .body(message, equalTo(unauthorizedErrorMessage))
        ;
    }
    
    @Test
    @Step("Проверка получения заказов авторизованного пользователя")
    public void authorizedGetOrdersTest() {
        // Создаем заказ с рандомным игредиентом
        ingredientsRequestObj = new IngredientsRequestObj(randomIngredient);
        clientCreateOrder.createOrder(accessToken, ingredientsRequestObj)
                .statusCode(200);
        
        // Достаем заказы по пользователю
        String totalField = "total";
        String totalTodayField = "totalToday";
        String ordersField = "orders.number";
        clientGetUsersOrder.getUsersOrder(accessToken)
                .statusCode(200)
                .body(success, equalTo(trueValue))
                .body(totalField, notNullValue())
                .body(totalTodayField, notNullValue())
                .body(ordersField, notNullValue())
        ;
    }
    
    @After
    @Step("Удаление созданного тестового пользователя")
    public void tearDown() {
        deleteUser(userObj);
    }
}