package clients.user;

import com.github.javafaker.Faker;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserFaker {
    
    private String email;
    private String password;
    private String name;
    private String randomString;
    
    public UserFaker() {
    }
    
    public void generateUserData() {
        Faker faker = new Faker();
        email = faker.internet().emailAddress();
        password = faker.internet().password();
        name = faker.name().firstName();
        randomString = faker.harryPotter().character();
    }
}
