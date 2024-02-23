package models.UserModels;

import lombok.Data;

@Data
public class UserInfoResponseModel {
    String id;
    String username;
    String firstName;
    String lastName;
    String email;
    String password;
    String phone;
    String userStatus;
}
