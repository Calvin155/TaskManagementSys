package com.example.TaskManagementSys.Controller;

import com.example.TaskManagementSys.DTO.UserDTO;
import com.example.TaskManagementSys.Entity.User;
import com.example.TaskManagementSys.JWTUtil.JwtUtil;
import com.example.TaskManagementSys.Service.UserService;
import lombok.Getter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    private final JwtUtil jwtUtil;

    public UserController(UserService userService, JwtUtil jwtUtil){
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/system-users")
    public ResponseEntity<List<UserDTO>> getAllUsers(){
        try{
            return ResponseEntity.ok().body(userService.getAllUsers());
        }catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }

}
