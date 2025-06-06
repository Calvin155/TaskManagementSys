package com.example.TaskManagementSys.Service;

import com.example.TaskManagementSys.Entity.RoleType;
import com.example.TaskManagementSys.Respository.RoleTypeRepository;
import org.springframework.stereotype.Service;

@Service
public class RoleTypeService {

    private final RoleTypeRepository roleTypeRepository;


    public RoleTypeService(RoleTypeRepository roleTypeRepository) {
        this.roleTypeRepository = roleTypeRepository;
    }

    public RoleType getRoleTypeByName(String roleType) {
        return roleTypeRepository.findByRoleTypeName(roleType);
    }
}
