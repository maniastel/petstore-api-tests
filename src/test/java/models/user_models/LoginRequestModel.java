package models.user_models;

import lombok.Data;

@Data
public class LoginRequestModel {
    String username, password;
}
