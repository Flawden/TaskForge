package com.flawden.TaskForgeAPI.controller;

import com.flawden.TaskForgeAPI.dto.AuthenticationResponse;
import com.flawden.TaskForgeAPI.dto.user.Login;
import com.flawden.TaskForgeAPI.dto.user.Register;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Контроллер аутентификации и регистрации пользователей.
 * <p>
 * Этот контроллер предоставляет два метода для регистрации нового пользователя и для входа
 * в систему с использованием учетных данных. Оба метода возвращают объект с информацией об
 * аутентификации, например, токен доступа.
 * </p>
 */
@Tag(name = "Аутентификация", description = "Контроллер для аутентификации и регистрации пользователей")
public interface AuthController {

    /**
     * Регистрация нового пользователя.
     * <p>
     * Этот метод обрабатывает запрос на регистрацию нового пользователя. После успешной регистрации
     * возвращается объект {@link AuthenticationResponse} с токеном доступа, который может быть использован
     * для последующих аутентификаций.
     * </p>
     *
     * @param register объект {@link Register}, содержащий данные для регистрации нового пользователя.
     * @return объект {@link AuthenticationResponse} с токеном аутентификации.
     */
    @Operation(
            summary = "Регистрация нового пользователя",
            description = "Позволяет зарегистрировать нового пользователя, предоставив имя, email и пароль."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь успешно зарегистрирован",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AuthenticationResponse.class))),
            @ApiResponse(responseCode = "400", description = "Некорректные данные регистрации",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера",
                    content = @Content)
    })
    ResponseEntity<AuthenticationResponse> register(@RequestBody Register register);

    /**
     * Вход в систему пользователя.
     * <p>
     * Этот метод обрабатывает запрос на вход в систему с использованием учетных данных. После успешного входа
     * возвращается объект {@link AuthenticationResponse} с токеном доступа, который может быть использован
     * для последующих аутентификаций.
     * </p>
     *
     * @param login объект {@link Login}, содержащий данные для входа пользователя в систему.
     * @return объект {@link AuthenticationResponse} с токеном аутентификации.
     */
    @Operation(
            summary = "Аутентификация пользователя",
            description = "Позволяет пользователю войти в систему, предоставив email и пароль."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь успешно аутентифицирован",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AuthenticationResponse.class))),
            @ApiResponse(responseCode = "401", description = "Неверные учетные данные",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера",
                    content = @Content)
    })
    ResponseEntity<AuthenticationResponse> login(@RequestBody Login login);

}
