package ru.netology.ibank;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import java.util.Locale;

import static io.restassured.RestAssured.given;

public class DataGenerator {
    private static RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();
    private static Faker faker = new Faker(new Locale("EN"));

    private static void sendRequest(CreateUser user) {
        given()
                .spec(requestSpec)
                .body(new CreateUser(
                        user.getLogin(),
                        user.getPassword(),
                        user.getStatus()))
                .when()
                .post("/api/system/users")
                .then()
                .statusCode(200);
    }

    public static String getRandomLogin() {
        return faker.name().username().replace(".", "");
    }

    public static String getRandomPassword() {
        return faker.internet().password();
    }

    public static class Registration {
        public static CreateUser getUser(String status) {
            return new CreateUser(getRandomLogin(), getRandomPassword(), status);
        }

        public static CreateUser getRegisteredUser(String status) {
            CreateUser registeredUser = getUser(status);
            sendRequest(registeredUser);
            return registeredUser;
        }
    }

}