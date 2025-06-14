package com.example.TaskManagementSys.DTO;

import com.example.TaskManagementSys.Entity.RoleType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Component
public class UserDTO {
    private Integer id;
    private String userName;
    private String roleType;
}
