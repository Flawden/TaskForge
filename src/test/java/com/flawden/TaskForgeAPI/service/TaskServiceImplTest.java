package com.flawden.TaskForgeAPI.service;

import com.flawden.TaskForgeAPI.dto.task.Priority;
import com.flawden.TaskForgeAPI.dto.task.Status;
import com.flawden.TaskForgeAPI.dto.task.Task;
import com.flawden.TaskForgeAPI.dto.user.Role;
import com.flawden.TaskForgeAPI.exception.UserAlreadyHaveThisTaskException;
import com.flawden.TaskForgeAPI.mapper.TaskMapper;
import com.flawden.TaskForgeAPI.model.TaskEntity;
import com.flawden.TaskForgeAPI.model.UserEntity;
import com.flawden.TaskForgeAPI.repository.TaskRepository;
import com.flawden.TaskForgeAPI.repository.UserRepository;
import com.flawden.TaskForgeAPI.service.impl.TaskServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TaskMapper taskMapper;

    @InjectMocks
    private TaskServiceImpl taskService;

    @Test
    void testGetAllTasks() {
        TaskEntity taskEntity = new TaskEntity(1L, "Задача 1", "Описание задачи 1", Status.PENDING, Priority.MEDIUM, new ArrayList<>(), new ArrayList<>());
        Task task = new Task(1L, "Задача 1", "Описание задачи 1", "PENDING", "MEDIUM", new ArrayList<>(), new ArrayList<>());
        when(taskRepository.findAll()).thenReturn(Collections.singletonList(taskEntity));
        when(taskMapper.mapTaskEntityToTask(any(TaskEntity.class))).thenReturn(task);
        List<Task> tasks = taskService.getAllTasks();
        assertNotNull(tasks);
        assertEquals(1, tasks.size());
        assertEquals("Задача 1", tasks.get(0).getTitle());
    }

    @Test
    void testGetTasksWithPagination() {
        TaskEntity taskEntity = new TaskEntity(1L, "Задача 1", "Описание задачи 1", Status.PENDING, Priority.MEDIUM, new ArrayList<>(), new ArrayList<>());
        Task task = new Task(1L, "Задача 1", "Описание задачи 1", "PENDING", "MEDIUM", new ArrayList<>(), new ArrayList<>());
        Pageable pageable = PageRequest.of(0, 5);
        when(taskRepository.findAll(pageable)).thenReturn(new PageImpl<>(Collections.singletonList(taskEntity)));
        when(taskMapper.mapTaskEntityToTask(any(TaskEntity.class))).thenReturn(task);
        List<Task> tasks = taskService.getTasksWithPagination(0, 5);
        assertNotNull(tasks);
        assertEquals(1, tasks.size());
        assertEquals("Задача 1", tasks.get(0).getTitle());
    }

    @Test
    void testGetTaskById() {
        Long taskId = 1L;
        TaskEntity taskEntity = new TaskEntity(1L, "Задача 1", "Описание задачи 1", Status.PENDING, Priority.MEDIUM, new ArrayList<>(), new ArrayList<>());
        Task task = new Task(1L, "Задача 1", "Описание задачи 1", "PENDING", "MEDIUM", new ArrayList<>(), new ArrayList<>());
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(taskEntity));
        when(taskMapper.mapTaskEntityToTask(any(TaskEntity.class))).thenReturn(task);
        Task result = taskService.getTaskById(taskId);
        assertNotNull(result);
        assertEquals(taskId, result.getId());
        assertEquals("Задача 1", result.getTitle());
    }

    @Test
    void testAddTask() {
        Task task = new Task(null, "Новая задача", "Описание новой задачи", "PENDING", "LOW", new ArrayList<>(), new ArrayList<>());
        TaskEntity taskEntity = new TaskEntity(1L, "Новая задача", "Описание новой задачи", Status.PENDING, Priority.LOW, new ArrayList<>(), new ArrayList<>());
        when(taskMapper.mapTaskToTaskEntity(any(Task.class))).thenReturn(taskEntity);
        when(taskRepository.save(any(TaskEntity.class))).thenReturn(taskEntity);
        when(taskMapper.mapTaskEntityToTask(any(TaskEntity.class))).thenReturn(task);
        Task result = taskService.addTask(task);
        assertNotNull(result);
        assertEquals("Новая задача", result.getTitle());
        assertEquals("PENDING", result.getStatus());
    }

    @Test
    void testUpdateTask() {
        Long taskId = 1L;
        Task task = new Task(taskId, "Обновленная задача", "Описание обновленной задачи", "IN_PROGRESS", "HIGH", new ArrayList<>(), new ArrayList<>());
        TaskEntity existingTaskEntity = new TaskEntity(taskId, "Задача 1", "Описание задачи 1", Status.PENDING, Priority.MEDIUM, new ArrayList<>(), new ArrayList<>());
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(existingTaskEntity));
        taskService.updateTask(task, taskId);
        assertEquals("Обновленная задача", existingTaskEntity.getTitle());
        assertEquals("IN_PROGRESS", existingTaskEntity.getStatus().name());
        assertEquals("HIGH", existingTaskEntity.getPriority().name());
    }

    @Test
    void testDeleteTask() {
        Long taskId = 1L;
        doNothing().when(taskRepository).deleteById(taskId);
        taskService.deleteTask(taskId);
        verify(taskRepository, times(1)).deleteById(taskId);
    }

    @Test
    void testGetTaskByName() {
        String taskName = "Задача 1";
        TaskEntity taskEntity = new TaskEntity(1L, taskName, "Описание задачи 1", Status.PENDING, Priority.MEDIUM, new ArrayList<>(), new ArrayList<>());
        Task task = new Task(1L, taskName, "Описание задачи 1", "PENDING", "MEDIUM", new ArrayList<>(), new ArrayList<>());
        when(taskRepository.findTaskEntitiesByTitle(taskName)).thenReturn(Optional.of(taskEntity));
        when(taskMapper.mapTaskEntityToTask(any(TaskEntity.class))).thenReturn(task);
        Task result = taskService.getTaskByName(taskName);
        assertNotNull(result);
        assertEquals(taskName, result.getTitle());
    }

    @Test
    void testAssignTaskToUser() {
        Long userId = 1L;
        Long taskId = 1L;
        UserEntity userEntity = new UserEntity(1L, "user@example.com", "password", "John", "Doe", Role.USER, new ArrayList<>());
        TaskEntity taskEntity = new TaskEntity(1L, "Задача 1", "Описание задачи 1", Status.PENDING, Priority.MEDIUM, new ArrayList<>(), new ArrayList<>());
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(taskEntity));
        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));
        taskService.assignTaskToUser(userId, taskId);
        assertTrue(userEntity.getTaskEntities().contains(taskEntity));
        verify(userRepository, times(1)).save(userEntity);
    }

    @Test
    void testAssignTaskToUser_AlreadyAssigned() {
        Long userId = 1L;
        Long taskId = 1L;
        UserEntity userEntity = new UserEntity(1L, "user@example.com", "password", "John", "Doe", Role.USER, new ArrayList<>());
        TaskEntity taskEntity = new TaskEntity(1L, "Задача 1", "Описание задачи 1", Status.PENDING, Priority.MEDIUM, new ArrayList<>(), new ArrayList<>());
        userEntity.getTaskEntities().add(taskEntity);
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(taskEntity));
        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));
        assertThrows(UserAlreadyHaveThisTaskException.class, () -> taskService.assignTaskToUser(userId, taskId));
    }
}


