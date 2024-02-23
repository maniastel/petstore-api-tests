package tests;

import data.TestData;
import models.StoreModels.CreateOrderResponseModel;
import models.StoreModels.ErrorResponseModel;
import models.StoreModels.GetOrderResponseModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static specs.StoreSpecs.CreateOrderSpec.createOrderRequestSpec;
import static specs.StoreSpecs.CreateOrderSpec.createOrderResponseSpec;
import static specs.StoreSpecs.GetOrderSpec.*;

@DisplayName("Тесты для проверки запросов /store")
@Tag("store_tests")
public class StoreTests extends TestBase {

    CreateOrderResponseModel orderData = new CreateOrderResponseModel();
    TestData data = new TestData();

    @Test
    @Tag("positive")
    @Tag("createOrder")
    @Tag("smoke")
    @DisplayName("Успешное оформление заказа")
    void successfulCreateOrderTest () {

        orderData.setId(data.id);
        orderData.setPetId(data.petId);
        orderData.setQuantity(data.quantity);
        orderData.setShipDate(data.shipDate);
        orderData.setStatus(data.status);
        orderData.setComplete(data.complete);


        CreateOrderResponseModel response = step("Send request", () ->
                given(createOrderRequestSpec)
                        .body(orderData)
                        .when()
                        .post()
                        .then()
                        .spec(createOrderResponseSpec)
                        .extract().as(CreateOrderResponseModel.class));

        step("Check response", () -> {
            assertEquals(orderData.getId(), response.getId());
            assertEquals(orderData.getPetId(), response.getPetId());
            assertEquals(orderData.getQuantity(), response.getQuantity());
            assertEquals(orderData.getShipDate(), response.getShipDate());
            assertEquals(orderData.getStatus(), response.getStatus());
            assertEquals(orderData.getComplete(), response.getComplete());
        });
    }

    @Test
    @Tag("positive")
    @Tag("getOrder")
    @Tag("smoke")
    @DisplayName("Получение данных о заказе")
    void getUserDataTest () {
        GetOrderResponseModel response = step("Send request", () ->
                given(getOrderRequestSpec)
                        .pathParam("orderId",11)
                        .when()
                        .get()
                        .then()
                        .spec(getOrderResponseSpec)
                        .extract().as(GetOrderResponseModel.class));

        step("Check response", () -> {
            assertEquals(data.id, response.getId());
            assertEquals(data.petId, response.getPetId());
            assertEquals(data.quantity, response.getQuantity());
            assertEquals(data.shipDate, response.getShipDate());
            assertEquals(data.status, response.getStatus());
            assertEquals(data.complete, response.getComplete());
        });
    }

    @Test
    @Tag("negative")
    @Tag("getOrder")
    @Tag("smoke")
    @DisplayName("Получение данных несуществующего заказа")
    void getNotExistedOrderDataTest () {
        ErrorResponseModel response = step("Send request", () ->
                given(getOrderRequestSpec)
                        .pathParam("orderId",7)
                        .when()
                        .get()
                        .then()
                        .spec(getOrder404ResponseSpec)
                        .extract().as(ErrorResponseModel.class));

        step("Check response", () -> {
            assertEquals("1", response.getCode());
            assertEquals("error", response.getType());
            assertEquals("Order not found", response.getMessage());
        });
    }
}
