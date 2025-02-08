package com.flawden.TaskForgeAPI.service;

import com.flawden.TaskForgeAPI.dto.AuthenticationResponse;
import com.flawden.TaskForgeAPI.dto.user.Login;
import com.flawden.TaskForgeAPI.dto.user.Register;
import com.flawden.TaskForgeAPI.exception.UserIsAlreadyExistException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * Интерфейс сервиса аутентификации и регистрации пользователей.
 * <p>
 * Этот сервис предоставляет методы для регистрации новых пользователей и входа в систему.
 * </p>
 */
public interface AuthService {

    /**
     * Регистрация нового пользователя.
     * <p>
     * Этот метод позволяет зарегистрировать нового пользователя в системе. В случае успешной регистрации
     * возвращается объект {@link AuthenticationResponse}, содержащий JWT токен для дальнейшей аутентификации.
     * </p>
     *
     * @param register объект, содержащий данные для регистрации пользователя.
     * @return объект {@link AuthenticationResponse} с JWT токеном.
     * @throws UserIsAlreadyExistException если пользователь с данным email уже существует.
     */
    AuthenticationResponse register(Register register);

    /**
     * Вход пользователя в систему.
     * <p>
     * Этот метод выполняет аутентификацию пользователя, проверяя его логин и пароль. После успешной аутентификации
     * возвращается объект {@link AuthenticationResponse} с JWT токеном для дальнейших запросов от имени этого пользователя.
     * </p>
     *
     * @param login объект, содержащий данные для входа (логин и пароль).
     * @return объект {@link AuthenticationResponse} с JWT токеном.
     * @throws UsernameNotFoundException если введенные учетные данные неверны.
     */
    AuthenticationResponse login(Login login);

}
