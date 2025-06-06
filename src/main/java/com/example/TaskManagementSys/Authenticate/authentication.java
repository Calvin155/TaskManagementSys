package com.example.TaskManagementSys.Authenticate;

import com.example.TaskManagementSys.Entity.User;
import com.example.TaskManagementSys.JWTUtil.JwtUtil;
import com.example.TaskManagementSys.Auth.LoginRequest;
import com.example.TaskManagementSys.Service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/authenticate")
public class authentication {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    public authentication(UserService userService, JwtUtil jwtUtil){
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login (@RequestBody LoginRequest loginRequest){
        User user = userService.getUser(loginRequest.getUsername(), loginRequest.getPassword());
        if (user != null) {
            String role = user.getRoleType().getRoleTypeName();
            final String jwt = jwtUtil.generateToken(user.getUserName(), role);
            return ResponseEntity.ok(Map.of("JWT Token", jwt));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/sign-up")
    public ResponseEntity<?> signUp (@ResponseBody )
}
