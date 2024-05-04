package clients.user.reg;

import clients.user.BaseUserClientTest;
import clients.user.UserFaker;
import clients.user.obj.UserRequestObj;
import io.qameta.allure.Step;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class ClientUserRegTest extends BaseUserClientTest {
    private UserRequestObj userObj;
    private UserRequestObj userObjWithNullEmail;
    private UserRequestObj userObjWithNullPassword;
    private UserRequestObj userObjWithNullName;
    
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
        
        // Создаем нового пользователя и записываем ответ
        userFaker.generateUserData();
        userObj = new UserRequestObj(userFaker.getEmail(), userFaker.getPassword(), userFaker.getName());
        validatableResponse = clientUser.userAdd(userObj).statusCode(200);
    }
    
    @Test
    @Step("Проверка успешного создания уникального пользователя")
    public void uniqueUserAddTest() {
        String accessTokenField = "accessToken";
        String refreshTokenField = "refreshToken";
        String userEmailField = "user.email";
        String userNameField = "user.name";
        validatableResponse
                .statusCode(200)
                .body(nameSuccessField, equalTo(trueValue))
                .body(userEmailField, equalTo(userFaker.getEmail()))
                .body(userNameField, equalTo(userFaker.getName()))
                .body(accessTokenField, notNullValue())
                .body(refreshTokenField, notNullValue())
        ;
    }
    
    @Test
    @Step("Проверка ошибки при попытке создания уже существующего пользователя")
    public void duplicateUserAddTest() {
        String duplicateUserMessage = "User already exists";
        clientUser.userAdd(userObj)
                .statusCode(403)
                .body(nameSuccessField, equalTo(falseValue))
                .body(messageField, equalTo(duplicateUserMessage))
        ;
    }
    
    @Test
    @Step("Проверка ошибки при попытке создания пользователя с пустым email")
    public void UserAddWithoutEmailTest() {
        String emptyRequiredFieldUserMessage = "Email, password and name are required fields";
        userObjWithNullEmail = new UserRequestObj(null, userFaker.getPassword(), userFaker.getName());
        clientUser.userAdd(userObjWithNullEmail)
                .statusCode(403)
                .body(nameSuccessField, equalTo(falseValue))
                .body(messageField, equalTo(emptyRequiredFieldUserMessage))
        ;
        
    }
    
    @Test
    @Step("Проверка ошибки при попытке создания пользователя с пустым password")
    public void UserAddWithoutPasswordTest() {
        String emptyRequiredFieldUserMessage = "Email, password and name are required fields";
        userObjWithNullPassword = new UserRequestObj(userFaker.getEmail(), null, userFaker.getName());
        clientUser.userAdd(userObjWithNullPassword)
                .statusCode(403)
                .body(nameSuccessField, equalTo(falseValue))
                .body(messageField, equalTo(emptyRequiredFieldUserMessage))
        ;
        
    }
    
    @Test
    @Step("Проверка ошибки при попытке создания пользователя с пустым name")
    public void UserAddWithoutNameTest() {
        String emptyRequiredFieldUserMessage = "Email, password and name are required fields";
        userObjWithNullName = new UserRequestObj(userFaker.getEmail(), userFaker.getPassword(), null);
        clientUser.userAdd(userObjWithNullName)
                .statusCode(403)
                .body(nameSuccessField, equalTo(falseValue))
                .body(messageField, equalTo(emptyRequiredFieldUserMessage))
        ;
        
    }
    
    @After
    @Step("Удаление созданного тестового пользователя")
    public void tearDown() {
        deleteUser(userObj);
    }
}