package clients.user;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;

public abstract class baseClient {
    protected static RequestSpecification requestSpec;
    
    protected baseClient() {
        requestSpec = new RequestSpecBuilder()
                .setBaseUri("https://stellarburgers.nomoreparties.site/")
                .addHeader("Content-Type", "application/json")
                .build();
    }
}