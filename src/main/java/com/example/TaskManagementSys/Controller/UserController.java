package com.example.TaskManagementSys.Controller;

import com.example.TaskManagementSys.DTO.NewUser;
import com.example.TaskManagementSys.DTO.UserDTO;
import com.example.TaskManagementSys.Entity.RoleType;
import com.example.TaskManagementSys.Entity.Task;
import com.example.TaskManagementSys.Entity.User;
import com.example.TaskManagementSys.JWTUtil.JwtUtil;
import com.example.TaskManagementSys.Service.RoleTypeService;
import com.example.TaskManagementSys.Service.UserService;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final RoleTypeService roleTypeService;

    private final JwtUtil jwtUtil;

    public UserController(UserService userService, JwtUtil jwtUtil, RoleTypeService roleTypeService){
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.roleTypeService = roleTypeService;
    }

    @GetMapping("/system-users")
    public ResponseEntity<List<UserDTO>> getAllUsers(){
        try{
            return ResponseEntity.ok().body(userService.getAllUsers());
        }catch (Exception e){
            System.out.println(e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }

    //Admin to be able to add new users
    @PostMapping("/system-users")
    public ResponseEntity<?> addNewUser(@RequestBody NewUser user){
        try{
            if (user == null){
                return ResponseEntity.badRequest().body("Invalid User");
            }
            userService.addNewUser(user);
            return ResponseEntity.ok().body("Successfully Added New User");
        }catch (Exception e){
            System.out.println(e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }

    //for updatinng system users - only admin access
    @PutMapping("/system-users")
    public ResponseEntity<?> updateUser(@RequestBody UserDTO userDTO){
        try {
            User user = userService.getUserByUserName(userDTO.getUserName());
            if (user == null){
                return ResponseEntity.badRequest().body("User not found");
            }

            user.setUserName(userDTO.getUserName());
            if (userDTO.getRoleType() != user.getRoleType().getRoleTypeName()){
                RoleType roleType = roleTypeService.getRoleTypeByName(userDTO.getRoleType());
                user.setRoleType(roleType);
            }
            userService.save(user);
            return ResponseEntity.ok("Successfully Updated User");
        } catch (Exception e){
            System.out.println(e.getMessage());
            return ResponseEntity.status(500).body("Internal Server Error");
        }
    }

    @DeleteMapping("/system-users")
    public ResponseEntity<?> deleteUser(@RequestParam String userName){
        try {
            User user = userService.getUserByUserName(userName);
            if (user != null){
                userService.deleteUser(user);
                return ResponseEntity.status(HttpStatus.OK).body("Successfully Deleted User: " + user.getUserName());
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cannot delete User");
        }catch (Exception e){
            System.out.println(e.getMessage());
            return ResponseEntity.status(500).body("Internal Server Error");
        }
    }


}
