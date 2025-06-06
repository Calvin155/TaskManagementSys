package com.example.TaskManagementSys.Respository;

import com.example.TaskManagementSys.Entity.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleTypeRepository extends JpaRepository<RoleType, Integer> {

    RoleType findByRoleTypeName(String roleTypeName);
}
