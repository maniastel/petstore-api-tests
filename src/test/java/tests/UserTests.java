package tests;

import data.TestUserData;
import models.user_models.LoginRequestModel;
import models.user_models.UserDataModel;
import models.user_models.UserResponseModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;
import static specs.user_specs.CreateUserSpec.*;
import static specs.user_specs.DeleteUserSpec.*;
import static specs.user_specs.GetUserSpec.*;
import static specs.user_specs.LoginSpec.loginRequestSpec;
import static specs.user_specs.LoginSpec.loginResponseSpec;
import static specs.user_specs.UpdateUserSpec.updateUserRequestSpec;
import static specs.user_specs.UpdateUserSpec.updateUserResponseSpec;

@DisplayName("Тесты для проверки запросов /user")
@Tag("user_tests")
public class UserTests extends TestBase {
    UserDataModel userData = new UserDataModel();
    LoginRequestModel login = new LoginRequestModel();
    TestUserData user = new TestUserData();

    @BeforeEach
    @DisplayName("Успешная регистрация нового пользователя")
    void successfulCreateUserTest () {

        userData.setId(user.id);
        userData.setUsername(user.username);
        userData.setFirstName(user.firstName);
        userData.setLastName(user.lastName);
        userData.setEmail(user.email);
        userData.setPhone(user.phone);
        userData.setPassword(user.password);
        userData.setUserStatus(user.userStatus);

        UserResponseModel response = step("Send request", () ->
                given(createUserRequestSpec)
                        .body(userData)
                        .when()
                        .post()
                        .then()
                        .spec(createUserResponseSpec)
                        .extract().as(UserResponseModel.class));

        step("Check response", () -> {
            assertEquals("200", response.getCode());
            assertEquals(userData.getId(), response.getMessage());
        });
    }

    @Test
    @Tag("negative")
    @Tag("createUser")
    @Tag("low")
    @DisplayName("Отправка запроса на создание пользователя с пустым телом")
    void CreateUserWithEmptyBodyTest () {
        UserResponseModel response = step("Send request", () ->
                given(createUserRequestSpec)
                        .body("{}")
                        .when()
                        .post()
                        .then()
                        .spec(createUserResponseSpec)
                        .extract().as(UserResponseModel.class));

        step("Check response", () -> {
            assertEquals("200", response.getCode());
            assertEquals("0", response.getMessage());
        });
    }

    @Test
    @Tag("positive")
    @Tag("getUser")
    @Tag("smoke")
    @DisplayName("Получение данных зарегистрованного пользователя")
    void getUserDataTest () {
        UserDataModel response = step("Send request", () ->
                given(getUserRequestSpec)
                        .pathParam("username",user.username)
                        .when()
                        .get()
                        .then()
                        .spec(getUserResponseSpec)
                        .extract().as(UserDataModel.class));

        step("Check response", () -> {
            assertEquals(user.username, response.getUsername());
            assertEquals(user.id, response.getId());
            assertEquals(user.firstName, response.getFirstName());
            assertEquals(user.lastName, response.getLastName());
            assertEquals(user.email, response.getEmail());
            assertEquals(user.password, response.getPassword());
            assertEquals(user.phone, response.getPhone());
            assertEquals(user.userStatus, response.getUserStatus());
        });
    }

    @Test
    @Tag("negative")
    @Tag("getUser")
    @Tag("smoke")
    @DisplayName("Получение данных несуществующего пользователя")
    void getNotExistedUserDataTest () {
        UserResponseModel response = step("Send request", () ->
                given(getUserRequestSpec)
                        .pathParam("username","testtest4567")
                        .when()
                        .get()
                        .then()
                        .spec(getUser404ResponseSpec)
                        .extract().as(UserResponseModel.class));

        step("Check response", () -> {
            assertEquals("1", response.getCode());
            assertEquals("error", response.getType());
            assertEquals("User not found", response.getMessage());
        });
    }

    @Test
    @Tag("positive")
    @Tag("putUser")
    @Tag("smoke")
    @DisplayName("Обновление данных пользователя")
    void updateUserDataTest () {

        userData.setId(user.id);
        userData.setUsername(user.username);
        userData.setFirstName(user.firstName);
        userData.setLastName(user.lastName);
        userData.setEmail("storm@test.com");
        userData.setPhone("79865643212");
        userData.setPassword(user.password);
        userData.setUserStatus(user.userStatus);

        UserResponseModel response = step("Send request", () ->
                given(updateUserRequestSpec)
                        .pathParam("username","nsokolof")
                        .body(userData)
                        .when()
                        .put()
                        .then()
                        .spec(updateUserResponseSpec)
                        .extract().as(UserResponseModel.class));

        step("Check response", () -> {
            assertEquals("200", response.getCode());
            assertEquals(userData.getId(), response.getMessage());
        });
    }

    @Test
    @Tag("positive")
    @Tag("loginUser")
    @Tag("smoke")
    @DisplayName("Успешная авторизация пользователя")
    void successfulLoginTest() {
        login.setUsername(user.username);
        login.setPassword(user.password);

        UserResponseModel response = step("Send request", () ->
                given(loginRequestSpec)
                        .param("username", login.getUsername())
                        .param("password", login.getPassword())
                        .when()
                        .get()
                        .then()
                        .spec(loginResponseSpec)
                        .extract().as(UserResponseModel.class));

        step("Check response", () -> {
            assertEquals("200", response.getCode());
            assertNotNull(response.getMessage());
        });
    }

    @Test
    @Tag("positive")
    @Tag("deleteUser")
    @Tag("smoke")
    @DisplayName("Успешное удаление пользователя")
    void successfulDeleteUserTest() {
        UserResponseModel response = step("Send request", () ->
                given(deleteUserRequestSpec)
                        .pathParam("username","nsokolof")
                        .when()
                        .delete()
                        .then()
                        .spec(deleteUserResponseSpec)
                        .extract().as(UserResponseModel.class));

        step("Check response", () -> {
            assertEquals("200", response.getCode());
            assertEquals("nsokolof", response.getMessage());
        });
    }

}
