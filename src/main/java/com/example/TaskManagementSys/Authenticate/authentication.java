package com.example.TaskManagementSys.Authenticate;
import com.example.TaskManagementSys.Auth.SignUpRequest;
import com.example.TaskManagementSys.Entity.RoleType;
import com.example.TaskManagementSys.Entity.User;
import com.example.TaskManagementSys.JWTUtil.JwtUtil;
import com.example.TaskManagementSys.Auth.LoginRequest;
import com.example.TaskManagementSys.Service.RoleTypeService;
import com.example.TaskManagementSys.Service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/authenticate")
public class authentication {

    private final UserService userService;
    private final RoleTypeService roleTypeService;
    private final JwtUtil jwtUtil;
    private static final String PASSWORD_PATTERN = "^(?=.*[A-Z])(?=.*[^a-zA-Z0-9]).{12,}$";
    private static final Pattern pattern = Pattern.compile(PASSWORD_PATTERN);

    public authentication(UserService userService, JwtUtil jwtUtil, RoleTypeService roleTypeService){
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.roleTypeService = roleTypeService;
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

    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(@RequestBody SignUpRequest signUpRequest){
        if (signUpRequest.getUsername() == null || signUpRequest.getPassword() == null ){
            return ResponseEntity.badRequest().build();
        }

        if (!isValidPassword(signUpRequest.getPassword())) {
            return ResponseEntity.badRequest().body("Invalid password format");
        }

        if(userService.checkUserNameExists(signUpRequest.getUsername())){
            return ResponseEntity.badRequest().body(HttpStatus.BAD_REQUEST + "Username Exists");
        }

        RoleType roleType = roleTypeService.getRoleTypeByName("USER");
        User user = new User();
        user.setUserName(signUpRequest.getUsername());
        user.setPassword(signUpRequest.getPassword());
        user.setRoleType(roleType);

        userService.signUpNewUser(user);

        return ResponseEntity.ok("Successfully Signed Up New User");
    }


    public static boolean isValidPassword(String password) {
        if (password == null) return false;
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }
}
