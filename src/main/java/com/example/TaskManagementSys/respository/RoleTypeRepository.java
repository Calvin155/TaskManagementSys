package com.example.TaskManagementSys.respository;

import com.example.TaskManagementSys.entity.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleTypeRepository extends JpaRepository<RoleType, Integer> {
}
