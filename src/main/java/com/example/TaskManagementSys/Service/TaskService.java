package com.example.TaskManagementSys.Service;

import com.example.TaskManagementSys.Entity.Task;
import com.example.TaskManagementSys.Entity.User;
import com.example.TaskManagementSys.Respository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    private TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository){
        this.taskRepository = taskRepository;
    }

    public List<Task> getUserTasks(User user){
        return taskRepository.findAllByUser_Id(user.getId());
    }
}
