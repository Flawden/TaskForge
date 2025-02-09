package com.flawden.TaskForgeAPI.service;

import com.flawden.TaskForgeAPI.dto.user.Role;
import com.flawden.TaskForgeAPI.dto.user.UpdateUser;
import com.flawden.TaskForgeAPI.dto.user.User;
import com.flawden.TaskForgeAPI.exception.UserNotFoundException;
import com.flawden.TaskForgeAPI.mapper.UserMapper;
import com.flawden.TaskForgeAPI.model.UserEntity;
import com.flawden.TaskForgeAPI.repository.UserRepository;
import com.flawden.TaskForgeAPI.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    private UserEntity userEntity;
    private User user;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        userEntity = new UserEntity(1L, "user@mail.ru", "password123", "John", "Doe", Role.USER, null);
        user = new User(1L, "user@mail.ru", "John", "Doe", "USER");
    }

    @Test
    public void testGetAllUsers() {
        when(userRepository.findAll()).thenReturn(List.of(userEntity));
        when(userMapper.mapUserEntityToUser(userEntity)).thenReturn(user);
        List<User> users = userService.getAllUsers();
        assertNotNull(users);
        assertEquals(1, users.size());
        assertEquals("user@mail.ru", users.get(0).getEmail());
    }

    @Test
    public void testGetUserById() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(userEntity));
        when(userMapper.mapUserEntityToUser(userEntity)).thenReturn(user);
        User result = userService.getUserById(1L);
        assertNotNull(result);
        assertEquals("user@mail.ru", result.getEmail());
    }

    @Test
    public void testGetUserById_UserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> userService.getUserById(1L));
    }

    @Test
    public void testAddUser() {
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);
        when(userMapper.mapUserToUserEntity(user)).thenReturn(userEntity);
        when(userMapper.mapUserEntityToUser(userEntity)).thenReturn(user);
        User result = userService.addUser(user);
        assertNotNull(result);
        assertEquals("user@mail.ru", result.getEmail());
    }

    @Test
    public void testUpdateUser() {
        UpdateUser updateUser = new UpdateUser("John", "Doe", "john.doe@example.com");
        when(userRepository.findById(1L)).thenReturn(Optional.of(userEntity));
        userService.updateUser(updateUser, 1L);
        verify(userRepository, times(1)).save(userEntity);
        assertEquals("john.doe@example.com", userEntity.getEmail());
    }

    @Test
    public void testUpdateUser_UserNotFound() {
        UpdateUser updateUser = new UpdateUser("John", "Doe", "john.doe@example.com");
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> userService.updateUser(updateUser, 1L));
    }

    @Test
    public void testDeleteUser() {
        userService.deleteUser(1L);
        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testGetUserByUsername() {
        when(userRepository.findByEmail("user@mail.ru")).thenReturn(Optional.of(userEntity));
        when(userMapper.mapUserEntityToUser(userEntity)).thenReturn(user);
        User result = userService.getUserByUsername("user@mail.ru");
        assertNotNull(result);
        assertEquals("user@mail.ru", result.getEmail());
    }

    @Test
    public void testGetUserByUsername_UserNotFound() {
        when(userRepository.findByEmail("user@mail.ru")).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> userService.getUserByUsername("user@mail.ru"));
    }
}
