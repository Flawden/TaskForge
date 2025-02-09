package com.flawden.TaskForgeAPI.service;

import com.flawden.TaskForgeAPI.config.security.JwtService;
import com.flawden.TaskForgeAPI.dto.AuthenticationResponse;
import com.flawden.TaskForgeAPI.dto.user.Login;
import com.flawden.TaskForgeAPI.dto.user.Register;
import com.flawden.TaskForgeAPI.exception.UserIsAlreadyExistException;
import com.flawden.TaskForgeAPI.mapper.UserMapper;
import com.flawden.TaskForgeAPI.model.UserEntity;
import com.flawden.TaskForgeAPI.repository.UserRepository;
import com.flawden.TaskForgeAPI.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserMapper userMapper;

    private AuthServiceImpl authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        authService = new AuthServiceImpl(userRepository, passwordEncoder, jwtService, authenticationManager, userMapper);
    }

    @Test
    void testRegisterSuccess() {
        Register register = new Register("john@example.com", "password123", "John", "Doe", "USER");
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(register.getUsername());
        userEntity.setPassword("encodedPassword");

        when(userRepository.findByEmail(register.getUsername())).thenReturn(java.util.Optional.empty());
        when(passwordEncoder.encode(register.getPassword())).thenReturn("encodedPassword");
        when(userMapper.mapRegisterToUserEntity(register)).thenReturn(userEntity);
        when(userRepository.save(userEntity)).thenReturn(userEntity);
        when(jwtService.generateToken(any())).thenReturn("mockJwtToken");

        AuthenticationResponse response = authService.register(register);

        assertNotNull(response);
        assertEquals("mockJwtToken", response.getToken());
        verify(userRepository, times(1)).findByEmail(register.getUsername());
        verify(userRepository, times(1)).save(userEntity);
    }

    @Test
    void testRegisterUserAlreadyExists() {
        Register register = new Register("john@example.com", "password123", "John", "Doe", "USER");
        when(userRepository.findByEmail(register.getUsername())).thenReturn(java.util.Optional.of(new UserEntity()));
        UserIsAlreadyExistException exception = assertThrows(UserIsAlreadyExistException.class, () -> authService.register(register));
        assertEquals("Пользователь с текущим электронным адресом уже существует", exception.getMessage());
        verify(userRepository, times(1)).findByEmail(register.getUsername());
        verify(userRepository, times(0)).save(any());
    }

    @Test
    void testLoginSuccess() {
        Login login = new Login("john.doe@example.com", "password");
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(login.getUsername());
        userEntity.setPassword("encodedPassword");

        when(authenticationManager.authenticate(any())).thenReturn(null); // mock successful authentication
        when(userRepository.findByEmail(login.getUsername())).thenReturn(java.util.Optional.of(userEntity));
        when(jwtService.generateToken(any())).thenReturn("mockJwtToken");

        AuthenticationResponse response = authService.login(login);

        assertNotNull(response);
        assertEquals("mockJwtToken", response.getToken());
        verify(authenticationManager, times(1)).authenticate(any());
        verify(userRepository, times(1)).findByEmail(login.getUsername());
    }

    @Test
    void testLoginUsernameNotFound() {
        Login login = new Login("john.doe@example.com", "password");
        when(authenticationManager.authenticate(any())).thenReturn(null); // mock successful authentication
        when(userRepository.findByEmail(login.getUsername())).thenReturn(java.util.Optional.empty());
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> authService.login(login));
        assertEquals("Bad credential", exception.getMessage());
        verify(authenticationManager, times(1)).authenticate(any());
        verify(userRepository, times(1)).findByEmail(login.getUsername());
    }
}

