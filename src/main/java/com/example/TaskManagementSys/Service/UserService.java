package com.example.TaskManagementSys.Service;

import com.example.TaskManagementSys.DTO.NewUser;
import com.example.TaskManagementSys.DTO.UserDTO;
import com.example.TaskManagementSys.Entity.Task;
import com.example.TaskManagementSys.Entity.User;
import com.example.TaskManagementSys.Respository.UserRepository;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    private UserRepository userRepository;
    private RoleTypeService roleTypeService;
    private static final PasswordEncoder psword = new BCryptPasswordEncoder();
    private final GoogleAuthenticator gAuth = new GoogleAuthenticator();

    public UserService(UserRepository userRepository, RoleTypeService roleTypeService){
        this.userRepository = userRepository;
        this.roleTypeService = roleTypeService;
    }

    public List<UserDTO> getAllUsers(){
        try {
            List<User> userList = userRepository.findAll();
            List<UserDTO> userDTOS = new ArrayList<>();
            for (User user : userList) {
                UserDTO userDTO = new UserDTO();
                userDTO.setId(user.getId());
                userDTO.setUserName(user.getUserName());
                userDTO.setRoleType(user.getRoleType().getRoleTypeName());
                userDTOS.add(userDTO);
            }
            return userDTOS;
        }catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    public User getUser(String userName, String password){
        User user = userRepository.findByUserName(userName);
        if (user == null){
            return null;
        }

        if (psword.matches(password, user.getPassword())){
            return user;
        }
        return null;
    }

    public User getUserByUserName(String userName){
        User user = userRepository.findByUserName(userName);
        if (user == null){
            return null;
        }

        return user;
    }

    public Boolean checkUserNameExists(String userName){
        User user = userRepository.findByUserName(userName);
        if (user != null){
            return true;
        }
        return false;
    }

    public void signUpNewUser(User user){
        User newUser = new User();
        newUser.setUserName(user.getUserName());
        String password = psword.encode(user.getPassword());
        newUser.setPassword(password);
        newUser.setRoleType(user.getRoleType());
        newUser.setTasks(new ArrayList<Task>());
        System.out.println(newUser.getUserName());
        userRepository.save(newUser);
    }

    public void save(User user){
        userRepository.save(user);
    }

    public void deleteUser(User user){
        userRepository.delete(user);
    }

    public void addNewUser(NewUser user){
        User newUser = new User();
        newUser.setUserName(user.getUserName());
        String password = psword.encode(user.getPassWord());
        newUser.setPassword(password);
        newUser.setTasks(new ArrayList<Task>());
        newUser.setRoleType(roleTypeService.getRoleTypeByName(user.getRoleType()));
        userRepository.save(newUser);
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        User user = userRepository.findByUserName(userName);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + userName);
        }

        List<GrantedAuthority> authorities = List.of(
                new SimpleGrantedAuthority("ROLE_" + user.getRoleType().getRoleTypeName())
        );

        return new org.springframework.security.core.userdetails.User(
                user.getUserName(),
                user.getPassword(),
                true, // accountNonExpired
                true, // credentialsNonExpired
                true, // accountNonLocked
                true, // enabled
                authorities
        );
    }

    public String generateSecretKey(String userName) {
        try {
            GoogleAuthenticatorKey key = gAuth.createCredentials();
            User user = userRepository.findByUserName(userName);

            user.setSecretKey(key.getKey());
            userRepository.save(user);

            return key.getKey();
        }catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    public String generateQrCodeUrl(String email, String secretKey) {
        return "otpauth://totp/MyApp:" + email + "?secret=" + secretKey + "&issuer=MyApp";
    }
}
