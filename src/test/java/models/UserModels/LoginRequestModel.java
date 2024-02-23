package models.UserModels;

import lombok.Data;

@Data
public class LoginRequestModel {
    String username, password;
}
