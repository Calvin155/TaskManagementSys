package com.example.TaskManagementSys.Authenticate;
import com.example.TaskManagementSys.Auth.SignUpRequest;
import com.example.TaskManagementSys.Entity.RoleType;
import com.example.TaskManagementSys.Entity.User;
import com.example.TaskManagementSys.JWTUtil.JwtUtil;
import com.example.TaskManagementSys.Auth.LoginRequest;
import com.example.TaskManagementSys.Service.RefreshTokenService;
import com.example.TaskManagementSys.Service.RoleTypeService;
import com.example.TaskManagementSys.Service.TokenBlacklistService;
import com.example.TaskManagementSys.Service.UserService;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
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
    private AuthenticationManager authenticationManager;
    private final GoogleAuthenticator googleAuthenticator = new GoogleAuthenticator();

    @Autowired
    private TokenBlacklistService tokenBlacklistService;
    private final RefreshTokenService refreshTokenService;

    public authentication(UserService userService, JwtUtil jwtUtil, RoleTypeService roleTypeService, TokenBlacklistService tokenBlacklistService, RefreshTokenService refreshTokenService){
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.roleTypeService = roleTypeService;
        this.tokenBlacklistService = tokenBlacklistService;
        this.refreshTokenService = refreshTokenService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login (@RequestBody LoginRequest loginRequest){
        try {
            User user = userService.getUser(loginRequest.getUsername(), loginRequest.getPassword());
            Integer otp = loginRequest.getOtp();

            if (otp == null) {
                return ResponseEntity.badRequest().body("OTP is required.");
            }

            if (!googleAuthenticator.authorize(user.getSecretKey(), otp)) {
                return ResponseEntity.status(401).body("Invalid OTP");
            }
            if (user != null) {
                String role = user.getRoleType().getRoleTypeName();
                tokenBlacklistService.blacklistPreviousTokens(loginRequest.getUsername());
                final String jwt = jwtUtil.generateToken(user.getUserName(), role);

                //store the newly issued token
                tokenBlacklistService.storeToken(loginRequest.getUsername(), jwt);
                //generate refresh token & return to client
                String refreshToken = refreshTokenService.createRefreshToken(loginRequest.getUsername());

                return ResponseEntity.ok(Map.of("JWT Access Token", jwt, "Refresh Token", refreshToken));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(@RequestBody SignUpRequest signUpRequest){
        try {
            if (signUpRequest.getUsername() == null || signUpRequest.getPassword() == null) {
                return ResponseEntity.badRequest().build();
            }

            if (!isValidPassword(signUpRequest.getPassword())) {
                return ResponseEntity.badRequest().body("Invalid password format");
            }

            if (userService.checkUserNameExists(signUpRequest.getUsername())) {
                return ResponseEntity.badRequest().body(HttpStatus.BAD_REQUEST + "Username Exists");
            }

            RoleType roleType = roleTypeService.getRoleTypeByName("USER");
            User user = new User();
            user.setUserName(signUpRequest.getUsername());
            user.setPassword(signUpRequest.getPassword());
            user.setRoleType(roleType);

            userService.signUpNewUser(user);

            return ResponseEntity.ok("Successfully Signed Up New User");
        }catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }


    public static boolean isValidPassword(String password) {
        if (password == null) return false;
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String accessToken, @RequestBody Map<String, String> request) {
        try {
            String refreshToken = request.get("refreshToken");

            if (accessToken == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Authorization header is required for logout");
            }

            //delete the refresh token from the db
            if (refreshToken != null) {
                refreshTokenService.revokeRefreshToken(refreshToken);
            }

            //blacklist the access token
            if (accessToken != null && accessToken.startsWith("Bearer ")) {
                tokenBlacklistService.blacklistToken(accessToken.substring(7));
            }

            return ResponseEntity.ok("Logged out successfully");
        }catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(401).body("Invalid or expired refresh token");
        }
    }
}
