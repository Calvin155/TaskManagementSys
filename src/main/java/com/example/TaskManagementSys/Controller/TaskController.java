package com.example.TaskManagementSys.Controller;

import com.example.TaskManagementSys.DTO.TaskDTO;
import com.example.TaskManagementSys.Entity.Task;
import com.example.TaskManagementSys.Entity.User;
import com.example.TaskManagementSys.JWTUtil.JwtUtil;
import com.example.TaskManagementSys.Service.TaskService;
import com.example.TaskManagementSys.Service.UserService;
import org.hibernate.internal.log.SubSystemLogging;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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

    //Get User tasks using the JWT Token
    @GetMapping("/task")
    public ResponseEntity<List<TaskDTO>> getUserTasks(@RequestHeader("Authorization") String jwtToken){
        try {
            User user = getUser(jwtToken);
            if (user == null) {
                return ResponseEntity.badRequest().build();
            }
            List<TaskDTO> allTasks = taskService.getUserTasks(user);
            if (allTasks == null) {
                return ResponseEntity.ok().body(null);
            }
            return ResponseEntity.ok().body(allTasks);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    //Add new task - JWT token to be passed as this identifies the user & the task is passed in the body
    @PostMapping("/task")
    public ResponseEntity<?> newUserTask(@RequestHeader("Authorization") String jwtToken, @RequestBody Task task){
        try {
            User user = getUser(jwtToken);
            if (user == null) {
                return ResponseEntity.badRequest().build();
            }

            if (task == null) {
                return ResponseEntity.badRequest().body("Task Properties Not Correctly Inserted");
            }

            Task newTask = new Task();
            newTask.setTitle(task.getTitle());
            newTask.setDescription(task.getDescription());
            newTask.setLevel(task.getLevel());
            newTask.setStatus(task.getStatus());
            newTask.setComplete(task.getComplete());
            newTask.setUser(user);
            taskService.addNewTask(newTask);

            return ResponseEntity.ok().body("Successfully Added New Task for user: " + user.getUserName());
        }catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    //User for update a task - Put header - JWT token to verify user & task in body with updates
    @PutMapping("/task")
    public ResponseEntity<?> updateTask(@RequestHeader("Authorization") String jwtToken, @RequestBody Task task){
        try {
            User user = getUser(jwtToken);
            if (task == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error Updating Task");
            }
            taskService.updateTask(task, user);
            return ResponseEntity.ok().body("Successfully Update Task");
        }catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    @DeleteMapping("/task")
    public ResponseEntity<?> deleteTask(@RequestHeader("Authorization") String jwtToken, @RequestBody Task task){
        try {


            User user = getUser(jwtToken);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please Re-Login & Try Again");
            }
            if (task == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error Deleting Task");
            }
            taskService.deleteTask(task);
            return ResponseEntity.ok().body("Successfully Deleted Task");
        }catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    //Validate user using JWT Token
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

    User getUser(String jwtToken){
        if (!validateUser(jwtToken)){
            return null;
        }
        String token = jwtToken.replace("Bearer ", "");
        String username = jwtUtil.extractUsername(token);
        User user = userService.getUserByUserName(username);
        if (user != null){
            return user;
        }
        return null;
    }
}
