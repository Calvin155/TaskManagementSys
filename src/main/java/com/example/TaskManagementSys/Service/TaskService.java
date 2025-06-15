package com.example.TaskManagementSys.Service;

import com.example.TaskManagementSys.DTO.TaskDTO;
import com.example.TaskManagementSys.Entity.Task;
import com.example.TaskManagementSys.Entity.User;
import com.example.TaskManagementSys.Respository.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    private TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository){
        this.taskRepository = taskRepository;
    }

    public List<TaskDTO> getUserTasks(User user){

        List<Task> allTasks = taskRepository.findAllByUser_Id(user.getId());
        List<TaskDTO> tasks = new ArrayList<>();

        if (allTasks == null){
            return null;
        }

        for(Task t : allTasks){
            TaskDTO taskDTO = new TaskDTO();
            taskDTO.setId(t.getId());
            taskDTO.setTitle(t.getTitle());
            taskDTO.setDescription(t.getDescription());
            taskDTO.setStatus(t.getStatus());
            taskDTO.setLevel(t.getLevel());
            taskDTO.setComplete(t.getComplete());
            tasks.add(taskDTO);
        }
        return tasks;
    }

    public void addNewTask(Task task){

        this.taskRepository.save(task);
    }

    @Transactional
    public void updateTask(Task task, User user) {
        taskRepository.updateTask(
                task.getId(),
                user.getId(),
                task.getTitle(),
                task.getLevel(),
                task.getStatus(),
                task.getDescription(),
                task.getComplete()
        );
    }


    public void deleteTask(Task task){
        this.taskRepository.delete(task);
    }
}
