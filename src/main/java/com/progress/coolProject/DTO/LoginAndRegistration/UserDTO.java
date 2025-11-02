package com.progress.coolProject.DTO.LoginAndRegistration;

import lombok.Data;

@Data
public class UserDTO {
    private String userName;
    private String password;
    private String firstName;
    private String lastName;
    private String middleName;
    private String phoneNumber;
    private Long roleId;
    private String email;
}
