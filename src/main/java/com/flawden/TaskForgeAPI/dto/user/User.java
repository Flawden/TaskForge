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
@Schema(description = "Представление пользователя в системе")
public class User {

    @Schema(description = "Идентификатор пользователя", example = "1")
    private Long id;

    @Email(message = "Адрес электронной почты не валиден")
    @NotNull(message = "Адрес электронной почты не может быть пустым")
    @Size(min = 5, message = "Электронная почта должна содержать минимум 5 символов")
    @Schema(description = "Электронная почта пользователя", example = "johndoe@example.com")
    private String email;

    @NotNull(message = "Имя не может быть пустым")
    @Size(min = 2, max = 50, message = "Имя должно содержать от 2 до 50 символов")
    @Schema(description = "Имя пользователя", example = "John")
    private String firstName;

    @NotNull(message = "Фамилия не может быть пустой")
    @Size(min = 2, max = 50, message = "Фамилия должна содержать от 2 до 50 символов")
    @Schema(description = "Фамилия пользователя", example = "Doe")
    private String lastName;

    @NotNull(message = "Роль не может быть пустой")
    @Size(min = 3, max = 20, message = "Роль должна содержать от 3 до 20 символов")
    @Schema(description = "Роль пользователя в системе", example = "USER")
    private String role;
}