package com.progress.coolProject.DTO.LoginAndRegistration;

import lombok.Data;

@Data
public class LoginOutput {
    private String role;
    private String userName;
    private String token;
    private Long userId;
}
