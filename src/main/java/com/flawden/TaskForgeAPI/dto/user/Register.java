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
@Schema(description = "Объект для передачи данных для регистрации нового пользователя")
public class Register {

    @NotNull(message = "Имя пользователя не может быть пустым")
    @Email(message = "Адрес электронной почты должен быть валидным")
    @Schema(description = "Имя пользователя для регистрации", example = "user@mail.ru")
    private String username;

    @NotNull(message = "Пароль не может быть пустым")
    @Size(min = 8, message = "Пароль должен содержать минимум 8 символов")
    @Schema(description = "Пароль пользователя для регистрации", example = "password123")
    private String password;

    @NotNull(message = "Имя пользователя не может быть пустым")
    @Schema(description = "Имя пользователя", example = "John")
    private String firstName;

    @NotNull(message = "Фамилия пользователя не может быть пустой")
    @Schema(description = "Фамилия пользователя", example = "Doe")
    private String lastName;

    @NotNull(message = "Роль пользователя не может быть пустой")
    @Schema(description = "Роль пользователя в системе", example = "USER")
    private String role;
}
