package com.example.TaskManagementSys.DTO;

import com.example.TaskManagementSys.Entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
public class TaskDTO {
    private Integer id;
    private String title;
    private String description;
    private String level;
    private String status;
    private Boolean complete;
}
