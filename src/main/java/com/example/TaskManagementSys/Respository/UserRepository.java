package com.example.TaskManagementSys.Respository;

import com.example.TaskManagementSys.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    User findByUserName(String userName);

}
