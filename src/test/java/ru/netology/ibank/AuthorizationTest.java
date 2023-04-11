package ru.netology.ibank;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.*;

import java.time.Duration;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.*;
import static ru.netology.ibank.DataGenerator.Registration.getRegisteredUser;
import static ru.netology.ibank.DataGenerator.Registration.getUser;
import static ru.netology.ibank.DataGenerator.getRandomLogin;
import static ru.netology.ibank.DataGenerator.getRandomPassword;

class AuthorizationTest {

    @BeforeEach
    void setUp() {
        Configuration.holdBrowserOpen = true;
        open("http://localhost:9999");
    }

    private final SelenideElement inputLogin = $("[data-test-id=\"login\"] input");
    private final SelenideElement inputPassword = $("[data-test-id=\"password\"] input");

    @Test
    void shouldSuccessfulAuthorization() {
        CreateUser registeredUser = getRegisteredUser("active");
        inputLogin.setValue(registeredUser.getLogin());
        inputPassword.setValue(registeredUser.getPassword());
        $(byText("Продолжить")).click();
        $(byText("Личный кабинет")).shouldBe(visible);
    }

    @Test
    void shouldUserIsBlocked() {
        CreateUser blockedUser = getRegisteredUser("blocked");
        inputLogin.setValue(blockedUser.getLogin());
        inputPassword.setValue(blockedUser.getPassword());
        $(byText("Продолжить")).click();
        $(byText("Ошибка")).shouldBe(visible, Duration.ofSeconds(10));
        $(withText("Пользователь заблокирован")).shouldBe(visible, Duration.ofSeconds(10));
    }

    @Test
    void shouldUserIsNotRegistered() {
        CreateUser notRegisteredUser = getUser("active");
        inputLogin.setValue(notRegisteredUser.getLogin());
        inputPassword.setValue(notRegisteredUser.getPassword());
        $(byText("Продолжить")).click();
        $(byText("Ошибка")).shouldBe(visible, Duration.ofSeconds(10));
        $(withText("Неверно указан логин или пароль")).shouldBe(visible, Duration.ofSeconds(10));
    }

    @Test
    void shouldInvalidLogin() {
        CreateUser registeredUserErrorLogin = getRegisteredUser("active");
        String errorLogin = getRandomPassword();
        inputLogin.setValue(errorLogin);
        inputPassword.setValue(registeredUserErrorLogin.getPassword());
        $(byText("Продолжить")).click();
        $(byText("Ошибка")).shouldBe(visible, Duration.ofSeconds(10));
        $(withText("Неверно указан логин или пароль")).shouldBe(visible, Duration.ofSeconds(10));
    }

    @Test
    void shouldInvalidPassword() {
        CreateUser registeredUserErrorPassword = getRegisteredUser("active");
        String errorPassword = getRandomLogin();
        inputLogin.setValue(registeredUserErrorPassword.getLogin());
        inputPassword.setValue(errorPassword);
        $(byText("Продолжить")).click();
        $(byText("Ошибка")).shouldBe(visible, Duration.ofSeconds(10));
        $(withText("Неверно указан логин или пароль")).shouldBe(visible, Duration.ofSeconds(10));
    }

    @Test
    void shouldNoDataEntered() {
        $(byText("Продолжить")).click();
        $(byText("Поле обязательно для заполнения")).shouldBe(visible);
    }

    @Test
    void shouldLoginNotEntered() {
        CreateUser registeredUser = getRegisteredUser("active");
        inputPassword.setValue(registeredUser.getPassword());
        $(byText("Продолжить")).click();
        $(byText("Поле обязательно для заполнения")).shouldBe(visible);
    }

    @Test
    void shouldPasswordNotEntered() {
        CreateUser registeredUser = getRegisteredUser("active");
        inputLogin.setValue(registeredUser.getLogin());
        $(byText("Продолжить")).click();
        $(byText("Поле обязательно для заполнения")).shouldBe(visible);
    }

}