package models.StoreModels;

import lombok.Data;

@Data
public class CreateOrderResponseModel {
    Integer id;
    Integer petId;
    Integer quantity;
    String shipDate;
    String status;
    Boolean complete;
}
