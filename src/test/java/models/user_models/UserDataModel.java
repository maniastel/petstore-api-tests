package models.user_models;

import lombok.Data;

@Data
public class UserDataModel {
    String id;
    String username;
    String firstName;
    String lastName;
    String email;
    String password;
    String phone;
    String userStatus;
}
