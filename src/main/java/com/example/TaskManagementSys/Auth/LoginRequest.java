package com.example.TaskManagementSys.Auth;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginRequest {
    private String username;
    private String password;
    private Integer otp;

    public LoginRequest() {}

    public LoginRequest(String username, String password, Integer otp) {
        this.username = username;
        this.password = password;
        this.otp = otp;
    }

}
