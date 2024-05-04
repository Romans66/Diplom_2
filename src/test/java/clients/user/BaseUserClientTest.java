package clients.user;

import clients.user.delete.ClientDelete;
import clients.user.login.ClientLogin;
import clients.user.obj.UserRequestObj;
import clients.user.reg.ClientUserReg;
import io.restassured.response.ValidatableResponse;

public class BaseUserClientTest {
    
    // Экземпляры клиентов
    protected ClientUserReg clientUser;
    protected ClientLogin clientLogin;
    protected UserFaker userFaker;
    protected ClientDelete clientDelete;
    protected ValidatableResponse validatableResponse;
    
    // Метод для удаления тестовых пользователей
    public void deleteUser(UserRequestObj userObj) {
        String successMessageForDeleteUser = "Тестовый пользователь успешно удален";
        String messageForEmptyUser = "Удаление пользователя не требуется";
        if (userObj != null) {
            clientLogin = new ClientLogin();
            clientDelete = new ClientDelete();
            
            String accessToken =
                    clientLogin.login(userObj).statusCode(200).extract().body().jsonPath().getString("accessToken");
            
            clientDelete.deleteUser(accessToken, userObj).statusCode(202);
            System.out.println(successMessageForDeleteUser);
        } else {
            System.err.println(messageForEmptyUser);
        }
    }
}
