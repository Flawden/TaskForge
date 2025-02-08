package com.flawden.TaskForgeAPI.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "Объект для передачи данных для обновления пользователя")
public class UpdateUser {

    @NotNull(message = "Имя пользователя не может быть пустым")
    @Schema(description = "Имя пользователя", example = "John")
    private String firstName;

    @NotNull(message = "Фамилия пользователя не может быть пустой")
    @Schema(description = "Фамилия пользователя", example = "Doe")
    private String lastName;

    @NotNull(message = "Электронная почта не может быть пустой")
    @Email(message = "Электронная почта должна быть валидным адресом")
    @Schema(description = "Электронная почта пользователя", example = "johndoe@example.com")
    private String email;
}
