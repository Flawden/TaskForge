package com.flawden.TaskForgeAPI.controller;

import com.flawden.TaskForgeAPI.dto.user.UpdateUser;
import com.flawden.TaskForgeAPI.dto.user.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Контроллер для работы с пользователями.
 * <p>
 * Этот контроллер предоставляет методы для получения, обновления, удаления пользователей,
 * а также для поиска пользователей по имени пользователя и получения всех пользователей с пагинацией.
 * </p>
 */
@Tag(name = "Пользователи", description = "Управление пользователями")
public interface UserController {

    /**
     * Получение списка всех пользователей с возможностью пагинации.
     * <p>
     * Этот метод позволяет получить список всех пользователей с пагинацией. Параметры {@code page} и {@code limit}
     * используются для ограничения количества возвращаемых пользователей. Если параметры не указаны,
     * то количество пользователей по умолчанию будет равно 5.
     * </p>
     *
     * @param page  номер страницы (опционально).
     * @param limit количество пользователей на странице (опционально, по умолчанию 5).
     * @return список пользователей с учетом пагинации.
     */
    @Operation(
            summary = "Получить всех пользователей",
            description = "Метод для получения списка всех пользователей с возможностью пагинации.",
            parameters = {
                    @Parameter(name = "page", description = "Номер страницы", required = false),
                    @Parameter(name = "size", description = "Количество пользователей на странице", required = false)
            },
            responses = {
                    @ApiResponse(
                            description = "Список пользователей",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = List.class)
                            )
                    )
            }
    )
    ResponseEntity<List<User>> getAllUsers(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "limit", required = false) Integer limit);

    /**
     * Получение пользователя по его ID.
     * <p>
     * Этот метод возвращает пользователя с указанным идентификатором.
     * </p>
     *
     * @param id идентификатор пользователя.
     * @return пользователь с заданным ID.
     */
    @Operation(
            summary = "Получить пользователя по ID",
            description = "Метод для получения пользователя по идентификатору.",
            parameters = @Parameter(name = "id", description = "Идентификатор пользователя", required = true),
            responses = {
                    @ApiResponse(
                            description = "Данные пользователя",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = User.class)
                            )
                    ),
                    @ApiResponse(
                            description = "Пользователь не найден",
                            responseCode = "404",
                            content = @Content
                    )
            }
    )
    ResponseEntity<User> getUserById(@PathVariable Long id);

    /**
     * Обновление данных пользователя.
     * <p>
     * Этот метод обрабатывает запрос на обновление данных пользователя с заданным ID.
     * </p>
     *
     * @param user   объект {@link UpdateUser}, содержащий обновленные данные пользователя.
     * @param userId идентификатор пользователя, которого необходимо обновить.
     * @return статус выполнения операции.
     */
    @Operation(
            summary = "Обновить данные пользователя",
            description = "Метод для обновления данных пользователя.",
            parameters = {
                    @Parameter(name = "userId", description = "Идентификатор пользователя", required = true)
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Данные для обновления пользователя",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UpdateUser.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            description = "Успешное обновление данных",
                            responseCode = "200",
                            content = @Content
                    ),
                    @ApiResponse(
                            description = "Пользователь не найден",
                            responseCode = "404",
                            content = @Content
                    )
            }
    )
    ResponseEntity<HttpStatus> updateUser(@RequestBody UpdateUser user, @PathVariable Long userId);

    /**
     * Удаление пользователя по его ID.
     * <p>
     * Этот метод удаляет пользователя с заданным идентификатором.
     * </p>
     *
     * @param id идентификатор пользователя.
     * @return статус выполнения операции.
     */
    @Operation(
            summary = "Удалить пользователя",
            description = "Метод для удаления пользователя по идентификатору.",
            parameters = @Parameter(name = "id", description = "Идентификатор пользователя", required = true),
            responses = {
                    @ApiResponse(
                            description = "Успешное удаление",
                            responseCode = "200",
                            content = @Content
                    ),
                    @ApiResponse(
                            description = "Пользователь не найден",
                            responseCode = "404",
                            content = @Content
                    )
            }
    )
    ResponseEntity<HttpStatus> deleteUser(@PathVariable Long id);

    /**
     * Получение пользователя по имени пользователя.
     * <p>
     * Этот метод возвращает пользователя с указанным именем пользователя.
     * </p>
     *
     * @param username имя пользователя.
     * @return пользователь с заданным именем.
     */
    @Operation(
            summary = "Получить пользователя по имени",
            description = "Метод для получения пользователя по имени.",
            parameters = @Parameter(name = "username", description = "Имя пользователя", required = true),
            responses = {
                    @ApiResponse(
                            description = "Данные пользователя",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = User.class)
                            )
                    ),
                    @ApiResponse(
                            description = "Пользователь не найден",
                            responseCode = "404",
                            content = @Content
                    )
            }
    )
    ResponseEntity<User> getUserByUsername(@PathVariable String username);

}
