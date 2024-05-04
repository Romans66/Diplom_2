package clients.user.login;

import clients.user.BaseUserClientTest;
import clients.user.UserFaker;
import clients.user.obj.UserRequestObj;
import clients.user.reg.ClientUserReg;
import io.qameta.allure.Step;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class ClientLoginTest extends BaseUserClientTest {
    private UserRequestObj userObj;
    private UserRequestObj userObjWithIncorrectData;
    
    // Поля и значения для запросов и ответов
    private final String nameSuccessField = "success";
    private final String messageField = "message";
    private final boolean trueValue = true;
    private final boolean falseValue = false;
    
    @Before
    @Step("Подготовка тестовых данных")
    public void setUp() {
        clientLogin = new ClientLogin();
        userFaker = new UserFaker();
        clientUser = new ClientUserReg();
        
        // Создаем нового пользователя и записываем ответ
        userFaker.generateUserData();
        userObj = new UserRequestObj(userFaker.getEmail(), userFaker.getPassword(), userFaker.getName());
        validatableResponse = clientUser.userAdd(userObj).statusCode(200);
    }
    
    @Test
    @Step("Проверка успешного логина существующего пользователя")
    public void loginTest() {
        String accessTokenField = "accessToken";
        String refreshTokenField = "refreshToken";
        String userEmailField = "user.email";
        String userNameField = "user.name";
        validatableResponse
                .statusCode(200)
                .body(nameSuccessField, equalTo(trueValue))
                .body(userEmailField, equalTo(userFaker.getEmail()))
                .body(userNameField, equalTo(userFaker.getName()))
                .body(accessTokenField, notNullValue()).body(refreshTokenField, notNullValue());
    }
    
    @Test
    @Step("Проверка ошибки при логине пользователя с неверным логином и паролем")
    public void loginWithIncorrectDataTest() {
        String incorrectDataLoginMessage = "email or password are incorrect";
        userObjWithIncorrectData = new UserRequestObj(userFaker.getEmail() + userFaker.getEmail(),
                userFaker.getPassword() + userFaker.getPassword(), userFaker.getName());
        clientLogin.login(userObjWithIncorrectData).statusCode(401).body(nameSuccessField, equalTo(falseValue)).body(messageField, equalTo(incorrectDataLoginMessage));
    }
    
    @After
    @Step("Удаление созданного тестового пользователя")
    public void tearDown() {
        deleteUser(userObj);
    }
}