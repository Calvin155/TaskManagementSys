package com.example.TaskManagementSys.Service;

import com.example.TaskManagementSys.Entity.User;
import com.example.TaskManagementSys.Respository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements UserDetailsService {

    private UserRepository userRepository;
    private static final PasswordEncoder psword = new BCryptPasswordEncoder();

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
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

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        User user = userRepository.findByUserName(userName); // assuming it returns User directly
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + userName);
        }

        List<GrantedAuthority> authorities = List.of(
                new SimpleGrantedAuthority("ROLE_" + user.getRoleType())
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


}
