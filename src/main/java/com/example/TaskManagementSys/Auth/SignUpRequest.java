package com.example.TaskManagementSys.Auth;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignUpRequest {
    private String userName;
    private String password;
    private String role_type;

    public SignUpRequest(String userName, String password){
        this.userName = userName;
        this.password = password;
        this.role_type = "USER";
    }

}
