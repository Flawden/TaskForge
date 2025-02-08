package com.flawden.TaskForgeAPI.controller.impl;

import com.flawden.TaskForgeAPI.controller.AuthController;
import com.flawden.TaskForgeAPI.dto.AuthenticationResponse;
import com.flawden.TaskForgeAPI.dto.user.Login;
import com.flawden.TaskForgeAPI.dto.user.Register;
import com.flawden.TaskForgeAPI.exception.UserIsAlreadyExistException;
import com.flawden.TaskForgeAPI.exception.UserNotFoundException;
import com.flawden.TaskForgeAPI.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Контроллер для аутентификации и регистрации пользователей.
 * <p>
 * Этот контроллер обрабатывает запросы на регистрацию новых пользователей и аутентификацию существующих пользователей.
 * </p>
 */
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthControllerImpl implements AuthController {

    private final AuthService authService;

    /**
     * Регистрация нового пользователя.
     * <p>
     * Метод позволяет пользователю зарегистрироваться, передав необходимые данные для регистрации в теле запроса.
     * </p>
     *
     * @param register Объект, содержащий данные для регистрации (например, имя, email, пароль).
     * @return Ответ с токеном аутентификации после успешной регистрации.
     */
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@Valid @RequestBody Register register) {
        return ResponseEntity.ok(authService.register(register));
    }

    /**
     * Аутентификация пользователя.
     * <p>
     * Метод позволяет пользователю войти в систему, предоставив данные для входа (например, email и пароль).
     * </p>
     *
     * @param login Объект, содержащий данные для аутентификации (например, email, пароль).
     * @return Ответ с токеном аутентификации после успешного входа.
     */
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> login(@Valid @RequestBody Login login) {
        return ResponseEntity.ok(authService.login(login));
    }

    /**
     * Обрабатывает исключение {@link UserIsAlreadyExistException}, которое возникает, когда пытаются зарегистрировать
     * пользователя с уже существующим в системе адресом электронной почты.
     * <p>
     * Этот метод перехватывает исключение {@link UserIsAlreadyExistException}, которое выбрасывается, если пользователь
     * пытается зарегистрироваться с адресом электронной почты, который уже существует в базе данных. Метод возвращает
     * ответ с кодом состояния 400 (BAD_REQUEST) и сообщением об ошибке.
     * </p>
     *
     * @param e Исключение {@link UserIsAlreadyExistException}, содержащее информацию об ошибке.
     * @return Ответ с кодом состояния 400 и сообщением об ошибке, объясняющим причину исключения.
     */
    @ExceptionHandler(UserIsAlreadyExistException.class)
    public ResponseEntity<String> handleUserIsAlreadyExistException(UserIsAlreadyExistException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    /**
     * Обработчик исключения, когда пользователь не найден.
     * <p>
     * Возвращает ошибку с кодом 404, если пользователь с данным идентификатором не найден.
     * </p>
     *
     * @param e исключение типа {@link UserNotFoundException}, которое возникает, если пользователь не найден.
     * @return сообщение об ошибке с соответствующим статусом 404.
     */
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
}
