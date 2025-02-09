package com.flawden.TaskForgeAPI.controller;

import com.flawden.TaskForgeAPI.controller.impl.UserControllerImpl;
import com.flawden.TaskForgeAPI.dto.user.UpdateUser;
import com.flawden.TaskForgeAPI.dto.user.User;
import com.flawden.TaskForgeAPI.exception.UserIsAlreadyExistException;
import com.flawden.TaskForgeAPI.exception.UserNotFoundException;
import com.flawden.TaskForgeAPI.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerImplTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserControllerImpl userController;

    @Test
    void testGetAllUsersWithoutPagination() {
        List<User> users = Arrays.asList(new User(1L, "johndoe@example.com", "John", "Doe", "USER"), new User(2L, "jane@example.com", "Jane", "Doe", "USER"));
        when(userService.getAllUsers()).thenReturn(users);
        ResponseEntity<List<User>> response = userController.getAllUsers(null, null);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(users, response.getBody());
        verify(userService, times(1)).getAllUsers();
    }

    @Test
    void testGetAllUsersWithPagination() {
        List<User> users = Arrays.asList(new User(1L, "johndoe@example.com", "John", "Doe", "USER"), new User(2L, "jane@example.com", "Jane", "Doe", "USER"));
        when(userService.getUsersWithPagination(0, 2)).thenReturn(users);
        ResponseEntity<List<User>> response = userController.getAllUsers(0, 2);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(users, response.getBody());
        verify(userService, times(1)).getUsersWithPagination(0, 2);
    }

    @Test
    void testGetUserById() {
        User user = new User(1L, "johndoe@example.com", "John", "Doe", "USER");
        when(userService.getUserById(1L)).thenReturn(user);
        ResponseEntity<User> response = userController.getUserById(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
        verify(userService, times(1)).getUserById(1L);
    }

    @Test
    void testUpdateUser() {
        UpdateUser updateUser = new UpdateUser("Johnny", "Doe", "johnny@example.com");
        doNothing().when(userService).updateUser(updateUser, 1L);
        ResponseEntity<HttpStatus> response = userController.updateUser(updateUser, 1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(userService, times(1)).updateUser(updateUser, 1L);
    }

    @Test
    void testDeleteUser() {
        doNothing().when(userService).deleteUser(1L);
        ResponseEntity<HttpStatus> response = userController.deleteUser(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(userService, times(1)).deleteUser(1L);
    }

    @Test
    void testGetUserByUsername() {
        User user = new User(1L, "johndoe@example.com", "John", "Doe", "USER");
        when(userService.getUserByUsername("john")).thenReturn(user);
        ResponseEntity<User> response = userController.getUserByUsername("john");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
        verify(userService, times(1)).getUserByUsername("john");
    }

    @Test
    void testHandleUserNotFoundException() {
        String errorMessage = "User not found";
        UserNotFoundException exception = new UserNotFoundException(errorMessage);
        ResponseEntity<String> response = userController.handleUserNotFoundException(exception);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(errorMessage, response.getBody());
    }

    @Test
    void testHandleUserIsAlreadyExistException() {
        String errorMessage = "User already exists";
        UserIsAlreadyExistException exception = new UserIsAlreadyExistException(errorMessage);
        ResponseEntity<String> response = userController.handleUserNotIsAlreadyExistException(exception);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(errorMessage, response.getBody());
    }
}
