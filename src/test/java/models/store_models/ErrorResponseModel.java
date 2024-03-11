package models.store_models;

import lombok.Data;

@Data
public class ErrorResponseModel {
    String code, type, message;
}
