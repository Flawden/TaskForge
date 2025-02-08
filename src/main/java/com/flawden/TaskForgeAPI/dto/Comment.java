package com.flawden.TaskForgeAPI.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Комментарий, привязанный к задаче")
public class Comment {

    @Schema(description = "Уникальный идентификатор комментария", example = "1")
    @Positive(message = "Идентификатор комментария должен быть положительным числом")
    private Long id;

    @Schema(description = "Текст комментария", example = "Это важное замечание к задаче.")
    @NotNull(message = "Текст комментария не может быть пустым")
    @Size(min = 1, max = 500, message = "Текст комментария должен быть от 1 до 500 символов")
    private String text;

    @Schema(description = "Идентификатор автора комментария", example = "123")
    @NotNull(message = "Идентификатор автора не может быть пустым")
    @Positive(message = "Идентификатор автора должен быть положительным числом")
    private Long author;

    @Schema(description = "Идентификатор задачи, к которой относится комментарий", example = "456")
    @NotNull(message = "Идентификатор задачи не может быть пустым")
    @Positive(message = "Идентификатор задачи должен быть положительным числом")
    private Long task;

}
