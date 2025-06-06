package com.example.TaskManagementSys.Auth;

import com.example.TaskManagementSys.Entity.RoleType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignUpRequest {
    private String username;
    private String password;

    public SignUpRequest(String username, String password){
        this.username = username;
        this.password = password;

    }

}
