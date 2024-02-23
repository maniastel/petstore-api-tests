package models.StoreModels;

import lombok.Data;

@Data
public class ErrorResponseModel {
    String code, type, message;
}
