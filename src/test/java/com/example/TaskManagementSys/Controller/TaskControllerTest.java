package com.example.TaskManagementSys.Controller;

import com.example.TaskManagementSys.DTO.TaskDTO;
import com.example.TaskManagementSys.Entity.Task;
import com.example.TaskManagementSys.Entity.User;
import com.example.TaskManagementSys.JWTUtil.JwtUtil;
import com.example.TaskManagementSys.Service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskControllerTest {

    @Mock
    private TaskService taskService;
    @Mock
    private JwtUtil jwtUtil;
    private TaskController taskController;
    private User mockUser;

    @BeforeEach
    void setup() {
        mockUser = new User();
        mockUser.setUserName("john");
        taskController = Mockito.spy(new TaskController(taskService, null, jwtUtil));
    }

    @Test
    void testGetUserTasks_UserIsNull_ReturnsBadRequest() {
        String token = "Bearer xyz";
        doReturn(null).when(taskController).getUser(token);
        ResponseEntity<List<TaskDTO>> response = taskController.getUserTasks(token);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testNewUserTask_ValidUserAndTask_ReturnsSuccess() {
        String token = "Bearer valid-token";
        Task task = new Task();
        task.setTitle("Test Task");
        task.setDescription("Description");
        task.setLevel("Medium");
        task.setStatus("In Progress");
        task.setComplete(false);
        doReturn(mockUser).when(taskController).getUser(token);
        ResponseEntity<?> response = taskController.newUserTask(token, task);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Successfully Added New Task for user: john", response.getBody());
        verify(taskService, times(1)).addNewTask(any(Task.class));
    }

    @Test
    void testNewUserTask_NullUser_ReturnsBadRequest() {
        String token = "Bearer invalid-token";
        Task task = new Task();
        doReturn(null).when(taskController).getUser(token);
        ResponseEntity<?> response = taskController.newUserTask(token, task);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testNewUserTask_NullTask_ReturnsBadRequestWithMessage() {
        String token = "Bearer valid-token";
        doReturn(mockUser).when(taskController).getUser(token);
        ResponseEntity<?> response = taskController.newUserTask(token, null);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Task Properties Not Correctly Inserted", response.getBody());
    }

    @Test
    void testNewUserTask_ExceptionThrown_ReturnsInternalServerError() {
        String token = "Bearer valid-token";
        Task task = new Task();
        doReturn(mockUser).when(taskController).getUser(token);
        doThrow(new RuntimeException("DB error")).when(taskService).addNewTask(any(Task.class));
        ResponseEntity<?> response = taskController.newUserTask(token, task);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Internal Server Error", response.getBody());
    }

}
