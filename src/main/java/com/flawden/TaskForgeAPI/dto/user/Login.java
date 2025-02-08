package com.flawden.TaskForgeAPI.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Объект для передачи данных для аутентификации пользователя")
public class Login {

    @NotNull(message = "Имя пользователя не может быть пустым")
    @Email(message = "Адрес электронной почты должен быть валидным")
    @Schema(description = "Имя пользователя для аутентификации", example = "user@mail.ru")
    private String username;

    @NotNull(message = "Пароль не может быть пустым")
    @Size(min = 8, message = "Пароль должен содержать минимум 8 символов")
    @Schema(description = "Пароль пользователя для аутентификации", example = "password123")
    private String password;
}
