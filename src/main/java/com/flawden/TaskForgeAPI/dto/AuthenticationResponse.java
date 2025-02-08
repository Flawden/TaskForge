package com.flawden.TaskForgeAPI.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Ответ на аутентификацию, содержащий токен доступа")
public class AuthenticationResponse {

    @NotNull(message = "Токен аутентификации не может быть пустым")
    @Schema(description = "Токен аутентификации, который используется для авторизации в системе", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIn0.SZsVpP0yO2wYqbGsOfEWrRYt4uWy9Erl7xt1Q7XJHTY")
    private String token;

}
