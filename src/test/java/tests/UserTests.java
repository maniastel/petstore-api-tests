package tests;

import configs.UserDataConfig;
import models.UserModels.LoginRequestModel;
import models.UserModels.UserDataModel;
import models.UserModels.UserInfoResponseModel;
import models.UserModels.UserResponseModel;
import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;
import static specs.UserSpecs.CreateUserSpec.*;
import static specs.UserSpecs.DeleteUserSpec.*;
import static specs.UserSpecs.GetUserSpec.*;
import static specs.UserSpecs.LoginSpec.loginRequestSpec;
import static specs.UserSpecs.LoginSpec.loginResponseSpec;
import static specs.UserSpecs.UpdateUserSpec.updateUserRequestSpec;
import static specs.UserSpecs.UpdateUserSpec.updateUserResponseSpec;

@DisplayName("Тесты для проверки запросов /user")
@Tag("user_tests")
public class UserTests extends TestBase {
    UserDataModel userData = new UserDataModel();
    LoginRequestModel login = new LoginRequestModel();
    UserDataConfig user = ConfigFactory.create(UserDataConfig.class, System.getProperties());

    @BeforeEach
    @DisplayName("Успешная регистрация нового пользователя")
    void successfulCreateUserTest () {

        userData.setId(user.setId());
        userData.setUsername(user.setUsername());
        userData.setFirstName(user.setFirstName());
        userData.setLastName(user.setLastName());
        userData.setEmail(user.setEmail());
        userData.setPhone(user.setPhone());
        userData.setPassword(user.setPassword());
        userData.setUserStatus(user.setUserStatus());

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
        UserInfoResponseModel response = step("Send request", () ->
                given(getUserRequestSpec)
                        .pathParam("username",user.setUsername())
                        .when()
                        .get()
                        .then()
                        .spec(getUserResponseSpec)
                        .extract().as(UserInfoResponseModel.class));

        step("Check response", () -> {
            assertEquals(user.setUsername(), response.getUsername());
            assertEquals(user.setId(), response.getId());
            assertEquals(user.setFirstName(), response.getFirstName());
            assertEquals(user.setLastName(), response.getLastName());
            assertEquals(user.setEmail(), response.getEmail());
            assertEquals(user.setPassword(), response.getPassword());
            assertEquals(user.setPhone(), response.getPhone());
            assertEquals(user.setUserStatus(), response.getUserStatus());
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

        userData.setId(user.setId());
        userData.setUsername(user.setUsername());
        userData.setFirstName(user.setFirstName());
        userData.setLastName(user.setLastName());
        userData.setEmail("storm@test.com");
        userData.setPhone("79865643212");
        userData.setPassword(user.setPassword());
        userData.setUserStatus(user.setUserStatus());

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
        login.setUsername(user.setUsername());
        login.setPassword(user.setPassword());

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
