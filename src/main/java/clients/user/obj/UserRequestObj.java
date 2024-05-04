package clients.user.obj;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserRequestObj {
    private String email;
    private String password;
    private String name;
}
