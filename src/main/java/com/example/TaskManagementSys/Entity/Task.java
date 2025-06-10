package com.example.TaskManagementSys.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Optional;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "task")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "title", nullable = false, unique = true)
    private String title;

    @Column(name = "description", nullable = false)
    private String description;

    //critcal & ok etc
    @Column(name = "level", nullable = false)
    private String level;

    //started, not started & complete
    @Column(name = "status", nullable = false)
    private String status;

    //yes or no - complete
    @Column(name = "complete", nullable = false)
    private Boolean complete;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
