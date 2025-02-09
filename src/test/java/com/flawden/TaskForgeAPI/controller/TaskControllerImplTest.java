package com.flawden.TaskForgeAPI.controller;

import com.flawden.TaskForgeAPI.controller.impl.TaskControllerImpl;
import com.flawden.TaskForgeAPI.dto.task.Task;
import com.flawden.TaskForgeAPI.exception.TaskNotFoundException;
import com.flawden.TaskForgeAPI.exception.UserAlreadyHaveThisTaskException;
import com.flawden.TaskForgeAPI.exception.UserNotFoundException;
import com.flawden.TaskForgeAPI.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskControllerImplTest {

    @Mock
    private TaskService taskService;

    @InjectMocks
    private TaskControllerImpl taskController;

    @BeforeEach
    void setUp() {
        taskController = new TaskControllerImpl(taskService);
    }

    @Test
    void testGetTaskById() {
        Long taskId = 1L;
        Task task = new Task();
        when(taskService.getTaskById(taskId)).thenReturn(task);
        ResponseEntity<Task> response = taskController.getTaskById(taskId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(task, response.getBody());
        verify(taskService, times(1)).getTaskById(taskId);
    }

    @Test
    void testAddTask() {
        Task task = new Task();
        when(taskService.addTask(task)).thenReturn(task);
        ResponseEntity<Task> response = taskController.addTask(task);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(task, response.getBody());
        verify(taskService, times(1)).addTask(task);
    }

    @Test
    void testUpdateTask() {
        Long taskId = 1L;
        Task task = new Task();
        doNothing().when(taskService).updateTask(task, taskId);
        ResponseEntity<HttpStatus> response = taskController.updateTask(task, taskId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(taskService, times(1)).updateTask(task, taskId);
    }

    @Test
    void testDeleteTask() {
        Long taskId = 1L;
        doNothing().when(taskService).deleteTask(taskId);
        ResponseEntity<HttpStatus> response = taskController.deleteTask(taskId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(taskService, times(1)).deleteTask(taskId);
    }

    @Test
    void testGetTaskByName() {
        String taskName = "Task Name";
        Task task = new Task();
        when(taskService.getTaskByName(taskName)).thenReturn(task);
        ResponseEntity<Task> response = taskController.getTaskByName(taskName);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(task, response.getBody());
        verify(taskService, times(1)).getTaskByName(taskName);
    }

    @Test
    void testAssignTaskToUser() {
        Long userId = 1L;
        Long taskId = 1L;
        doNothing().when(taskService).assignTaskToUser(userId, taskId);
        ResponseEntity<HttpStatus> response = taskController.assignTaskToUser(userId, taskId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(taskService, times(1)).assignTaskToUser(userId, taskId);
    }

    @Test
    void testHandleUserAlreadyHaveThisTaskException() {
        String errorMessage = "User already has this task";
        UserAlreadyHaveThisTaskException exception = new UserAlreadyHaveThisTaskException(errorMessage);
        doThrow(exception).when(taskService).assignTaskToUser(anyLong(), anyLong());
        try {
            taskController.assignTaskToUser(1L, 1L);
            fail("Expected exception to be thrown");
        } catch (UserAlreadyHaveThisTaskException e) {
            assertEquals(errorMessage, e.getMessage());
        }
        verify(taskService, times(1)).assignTaskToUser(anyLong(), anyLong());
    }

    @Test
    void testHandleTaskNotFoundException() {
        String errorMessage = "Task not found";
        TaskNotFoundException exception = new TaskNotFoundException(errorMessage);
        when(taskService.getTaskById(anyLong())).thenThrow(exception);
        try {
            taskController.getTaskById(1L);
            fail("Expected exception to be thrown");
        } catch (TaskNotFoundException e) {
            assertEquals(errorMessage, e.getMessage());
        }
        verify(taskService, times(1)).getTaskById(anyLong());
    }

    @Test
    void testHandleUserNotFoundException() {
        String errorMessage = "User not found";
        UserNotFoundException exception = new UserNotFoundException(errorMessage);
        doThrow(exception).when(taskService).assignTaskToUser(anyLong(), anyLong());
        try {
            taskController.assignTaskToUser(1L, 1L);
            fail("Expected exception to be thrown");
        } catch (UserNotFoundException e) {
            assertEquals(errorMessage, e.getMessage());
        }
        verify(taskService, times(1)).assignTaskToUser(anyLong(), anyLong());
    }
}

