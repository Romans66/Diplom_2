package clients.user.updatedata;

import clients.user.BaseUserClientTest;
import clients.user.UserFaker;
import clients.user.login.ClientLogin;
import clients.user.obj.UserRequestObj;
import clients.user.reg.ClientUserReg;
import io.qameta.allure.Step;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;

public class ClientUpdateDataTest extends BaseUserClientTest {
    private UserRequestObj userObj;
    private ClientDataUpdate clientDataUpdate;
    private String accessToken;
    
    // Поля и значения для запросов и ответов
    private final String nameSuccessField = "success";
    private final String messageField = "message";
    private final boolean trueValue = true;
    private final boolean falseValue = false;
    
    @Before
    @Step("Подготовка тестовых данных")
    public void setUp() {
        userFaker = new UserFaker();
        clientUser = new ClientUserReg();
        clientLogin = new ClientLogin();
        clientDataUpdate = new ClientDataUpdate();
        
        // Создаем нового пользователя и записываем ответ
        userFaker.generateUserData();
        userObj = new UserRequestObj(userFaker.getEmail(), userFaker.getPassword(), userFaker.getName());
        validatableResponse = clientUser.userAdd(userObj).statusCode(200);
        accessToken =
                clientLogin.login(userObj).statusCode(200).extract().body().jsonPath().getString("accessToken");
    }
    
    @Test
    @Step("Проверка успешного изменения данных авторизованного пользователя")
    public void updateAuthorizedUserDataTest() {
        String userEmailField = "user.email";
        String userNameField = "user.name";
        String updateEmail = userFaker.getEmail() + userFaker.getRandomString();
        String updateName = userFaker.getName() + userFaker.getRandomString();
        userObj.setEmail(updateEmail.toLowerCase());
        userObj.setName(updateName.toLowerCase());
        clientDataUpdate.dataUpdateUser(accessToken, userObj)
                .statusCode(200)
                .body(nameSuccessField, equalTo(trueValue))
                .body(userEmailField, equalTo(updateEmail.toLowerCase()))
                .body(userNameField, equalTo(updateName.toLowerCase()))
        ;
    }
    
    @Test
    @Step("Проверка ошибки при изменении данных под неавторизованным пользователем")
    public void updateUnauthorizedUserDataTest() {
        String updateUnauthorizedUserDataMessage = "You should be authorised";
        String emptyAccessToken = "";
        clientDataUpdate.dataUpdateUser(emptyAccessToken, userObj)
                .statusCode(401)
                .body(nameSuccessField, equalTo(falseValue))
                .body(messageField, equalTo(updateUnauthorizedUserDataMessage))
        ;
    }
    
    @After
    @Step("Удаление созданного тестового пользователя")
    public void tearDown() {
        deleteUser(userObj);
    }
}