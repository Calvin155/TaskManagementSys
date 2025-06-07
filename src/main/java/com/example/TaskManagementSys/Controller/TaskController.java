package com.example.TaskManagementSys.Controller;

import com.example.TaskManagementSys.Entity.Task;
import com.example.TaskManagementSys.Entity.User;
import com.example.TaskManagementSys.JWTUtil.JwtUtil;
import com.example.TaskManagementSys.Service.TaskService;
import com.example.TaskManagementSys.Service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    public TaskController(TaskService taskService,UserService userService, JwtUtil jwtUtil){
        this.taskService = taskService;
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/task")
    public ResponseEntity<List<Task>> getUserTasks(@RequestHeader("Authorization") String jwtToken){
        String token = jwtToken.replace("Bearer ", "");
        String username = jwtUtil.extractUsername(token);
        User user = userService.getUserByUserName(username);
        List<Task> allTasks = taskService.getUserTasks(user);

        if(allTasks == null){
            return ResponseEntity.ok().body(null);
        }

        return ResponseEntity.ok().body(allTasks);
    }

    private boolean validateUser(String token) {
        try {
            String jwtToken = token.replace("Bearer ", "");
            String username = jwtUtil.extractUsername(jwtToken);

            return jwtUtil.validateToken(jwtToken, username);
        } catch (Exception e) {
            System.out.println("Token validation error: " + e.getMessage());
            return false;
        }
    }
}
