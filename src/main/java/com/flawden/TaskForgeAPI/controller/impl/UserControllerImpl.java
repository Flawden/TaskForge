package com.flawden.TaskForgeAPI.controller.impl;

import com.flawden.TaskForgeAPI.controller.UserController;
import com.flawden.TaskForgeAPI.dto.user.UpdateUser;
import com.flawden.TaskForgeAPI.dto.user.User;
import com.flawden.TaskForgeAPI.exception.UserIsAlreadyExistException;
import com.flawden.TaskForgeAPI.exception.UserNotFoundException;
import com.flawden.TaskForgeAPI.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Контроллер для работы с пользователями.
 * <p>
 * Этот контроллер предоставляет функционал для получения, обновления, удаления пользователей,
 * а также для поиска пользователя по имени.
 * </p>
 */
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserControllerImpl implements UserController {

    private final UserService userService;

    /**
     * Получить всех пользователей с возможностью пагинации.
     * <p>
     * Если параметры пагинации не переданы, возвращаются все пользователи. В противном случае, данные
     * пользователи возвращаются с учетом заданных значений страницы и размера страницы.
     * </p>
     *
     * @param page номер страницы (необязательный параметр).
     * @param size количество пользователей на одной странице (необязательный параметр).
     * @return список пользователей.
     */
    @GetMapping
    @Override
    public ResponseEntity<List<User>> getAllUsers(Integer page, Integer size) {
        if (page == null) {
            return ResponseEntity.ok(userService.getAllUsers());
        } else {
            return ResponseEntity.ok(userService.getUsersWithPagination(page, size));
        }
    }

    /**
     * Получить пользователя по идентификатору.
     * <p>
     * Возвращает пользователя, соответствующего переданному идентификатору.
     * </p>
     *
     * @param id идентификатор пользователя.
     * @return пользователь с данным идентификатором.
     */
    @GetMapping("/{id}")
    @Override
    public ResponseEntity<User> getUserById(Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    /**
     * Обновить данные пользователя.
     * <p>
     * Обновляет данные пользователя с указанным идентификатором.
     * </p>
     *
     * @param user   обновленные данные пользователя.
     * @param userId идентификатор пользователя, чьи данные нужно обновить.
     * @return HTTP статус успешного обновления.
     */
    @PatchMapping("/{userId}")
    @Override
    public ResponseEntity<HttpStatus> updateUser(@Valid UpdateUser user, Long userId) {
        userService.updateUser(user, userId);
        return ResponseEntity.ok().build();
    }

    /**
     * Удалить пользователя.
     * <p>
     * Удаляет пользователя по идентификатору.
     * </p>
     *
     * @param id идентификатор пользователя для удаления.
     * @return HTTP статус успешного удаления.
     */
    @DeleteMapping("/{id}")
    @Override
    public ResponseEntity<HttpStatus> deleteUser(Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }

    /**
     * Получить пользователя по имени пользователя.
     * <p>
     * Возвращает пользователя, соответствующего переданному имени.
     * </p>
     *
     * @param username имя пользователя.
     * @return пользователь с данным именем.
     */
    @GetMapping("/user/{username}")
    @Override
    public ResponseEntity<User> getUserByUsername(String username) {
        return ResponseEntity.ok(userService.getUserByUsername(username));
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

    /**
     * Обработчик исключения, когда пользователь уже существует.
     * <p>
     * Возвращает ошибку с кодом 404, если попытка создания пользователя с таким именем не удалась.
     * </p>
     *
     * @param e исключение типа {@link UserIsAlreadyExistException}, которое возникает, если пользователь уже существует.
     * @return сообщение об ошибке с соответствующим статусом 404.
     */
    @ExceptionHandler(UserIsAlreadyExistException.class)
    public ResponseEntity<String> handleUserNotIsAlreadyExistException(UserIsAlreadyExistException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

}
