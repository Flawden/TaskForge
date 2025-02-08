package com.flawden.TaskForgeAPI.service.impl;

import com.flawden.TaskForgeAPI.config.security.JwtService;
import com.flawden.TaskForgeAPI.config.security.PersonDetails;
import com.flawden.TaskForgeAPI.dto.AuthenticationResponse;
import com.flawden.TaskForgeAPI.dto.user.Login;
import com.flawden.TaskForgeAPI.dto.user.Register;
import com.flawden.TaskForgeAPI.exception.UserIsAlreadyExistException;
import com.flawden.TaskForgeAPI.mapper.UserMapper;
import com.flawden.TaskForgeAPI.model.UserEntity;
import com.flawden.TaskForgeAPI.repository.UserRepository;
import com.flawden.TaskForgeAPI.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Реализация сервиса аутентификации и регистрации пользователей.
 * <p>
 * Этот сервис управляет процессами регистрации и входа пользователя в систему, включая проверку уникальности
 * адреса электронной почты, хэширование пароля и генерацию токенов JWT для аутентификации.
 * </p>
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserMapper userMapper;

    /**
     * Регистрация нового пользователя.
     * <p>
     * Этот метод проверяет наличие пользователя с указанным email. Если такой пользователь уже существует,
     * выбрасывается исключение {@link UserIsAlreadyExistException}. После этого пароль пользователя
     * кодируется, и он сохраняется в базе данных. Генерируется JWT токен для нового пользователя.
     * </p>
     *
     * @param register данные для регистрации нового пользователя.
     * @return объект {@link AuthenticationResponse}, содержащий JWT токен.
     * @throws UserIsAlreadyExistException если пользователь с указанным email уже существует.
     */
    @Override
    @Transactional
    public AuthenticationResponse register(Register register) {
        if (userRepository.findByEmail(register.getUsername()).isPresent()) {
            throw new UserIsAlreadyExistException("Пользователь с текущим электронным адресом уже существует");
        }
        register.setPassword(passwordEncoder.encode(register.getPassword()));
        UserEntity user = userMapper.mapRegisterToUserEntity(register);
        userRepository.save(user);
        return AuthenticationResponse.builder()
                .token(jwtService.generateToken(new PersonDetails(user)))
                .build();
    }

    /**
     * Аутентификация пользователя по email и паролю.
     * <p>
     * Этот метод выполняет аутентификацию пользователя с помощью {@link AuthenticationManager}, проверяя его
     * учетные данные. После успешной аутентификации генерируется JWT токен для пользователя.
     * </p>
     *
     * @param login данные для входа пользователя, включающие email и пароль.
     * @return объект {@link AuthenticationResponse}, содержащий JWT токен.
     * @throws UsernameNotFoundException если учетные данные неверны.
     */
    @Override
    public AuthenticationResponse login(Login login) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(login.getUsername(), login.getPassword()));
        UserEntity user = userRepository.findByEmail(login.getUsername()).orElseThrow(() -> new UsernameNotFoundException("Bad credential"));
        return AuthenticationResponse.builder()
                .token(jwtService.generateToken(new PersonDetails(user)))
                .build();
    }
}
