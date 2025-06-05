package com.example.TaskManagementSys.controller;

import com.example.TaskManagementSys.entity.User;
import com.example.TaskManagementSys.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    public UserController(UserService userService){
        this.userService = userService;
    }

    //LOGIN - Issue basic JWT
    //LOG-OUT//
}
