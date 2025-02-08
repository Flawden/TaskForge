package com.flawden.TaskForgeAPI.dto.task;

import com.flawden.TaskForgeAPI.dto.Comment;
import com.flawden.TaskForgeAPI.dto.user.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Объект, представляющий задачу в системе")
public class Task {

    @Schema(description = "Уникальный идентификатор задачи", example = "1")
    private Long id;

    @NotNull(message = "Название задачи не может быть пустым")
    @Size(min = 1, max = 100, message = "Название задачи должно быть от 1 до 100 символов")
    @Schema(description = "Название задачи", example = "Задача 1")
    private String title;

    @Size(max = 500, message = "Описание задачи не может быть длиннее 500 символов")
    @Schema(description = "Описание задачи", example = "Описание задачи 1")
    private String description;

    @NotNull(message = "Статус задачи не может быть пустым")
    @Schema(description = "Статус задачи", allowableValues = {"TODO", "IN_PROGRESS", "DONE"}, example = "TODO")
    private String status;

    @NotNull(message = "Приоритет задачи не может быть пустым")
    @Schema(description = "Приоритет задачи", allowableValues = {"LOW", "MEDIUM", "HIGH"}, example = "MEDIUM")
    private String priority;

    @Schema(description = "Список исполнителей задачи")
    private List<User> executors;

    @Schema(description = "Список комментариев к задаче")
    private List<Comment> comments;

}
