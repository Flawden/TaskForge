package com.flawden.TaskForgeAPI.controller;

import com.flawden.TaskForgeAPI.dto.task.Status;
import com.flawden.TaskForgeAPI.dto.task.Task;
import com.flawden.TaskForgeAPI.dto.task.Priority;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Контроллер для работы с задачами.
 * <p>
 * Этот контроллер предоставляет методы для получения, добавления, обновления и удаления задач.
 * Также есть возможности для получения задач по имени и назначения задачи пользователю.
 * </p>
 */
@Tag(name = "Задачи", description = "Управление задачами")
public interface TaskController {

    /**
     * Получение списка всех задач с возможностью пагинации.
     * <p>
     * Этот метод позволяет получить все задачи с пагинацией. Параметры {@code page} и {@code limit}
     * используются для ограничения количества возвращаемых задач. Если параметры не указаны,
     * то количество задач по умолчанию будет равно 5.
     * </p>
     *
     * @param page  номер страницы (опционально).
     * @param limit количество задач на странице (опционально, по умолчанию 5).
     * @return список задач с учетом пагинации.
     */
    @Operation(summary = "Получить все задачи", description = "Получить все задачи с возможностью пагинации.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Задачи успешно получены"),
            @ApiResponse(responseCode = "400", description = "Неверный запрос")
    })
    ResponseEntity<List<Task>> getAllTasks(@RequestParam(value = "page", required = false) Integer page,
                                           @RequestParam(value = "limit", required = false, defaultValue = "5") Integer limit,
                                           @RequestParam(value = "title", required = false) String title,
                                           @RequestParam(value = "status", required = false) Status status,
                                           @RequestParam(value = "priority", required = false) Priority priority);

    /**
     * Получение задачи по её ID.
     * <p>
     * Этот метод возвращает задачу с указанным идентификатором.
     * </p>
     *
     * @param id идентификатор задачи.
     * @return задача с заданным ID.
     */
    @Operation(summary = "Получить задачу по идентификатору", description = "Возвращает задачу по переданному идентификатору.")
    ResponseEntity<Task> getTaskById(@PathVariable Long id);

    /**
     * Добавление новой задачи.
     * <p>
     * Этот метод обрабатывает запрос на добавление новой задачи.
     * </p>
     *
     * @param task объект {@link Task}, содержащий данные для создания новой задачи.
     * @return добавленная задача.
     */
    @Operation(summary = "Добавить новую задачу", description = "Добавляет новую задачу в систему.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Задача успешно добавлена"),
            @ApiResponse(responseCode = "400", description = "Неверные данные")
    })
    ResponseEntity<Task> addTask(@RequestBody Task task);

    /**
     * Обновление существующей задачи.
     * <p>
     * Этот метод обрабатывает запрос на обновление существующей задачи.
     * </p>
     *
     * @param task   объект {@link Task}, содержащий обновленные данные задачи.
     * @param taskId идентификатор задачи, которую необходимо обновить.
     * @return статус выполнения операции.
     */
    @Operation(summary = "Обновить задачу", description = "Обновляет задачу с указанным идентификатором.")
    ResponseEntity<HttpStatus> updateTask(@RequestBody Task task, @PathVariable Long taskId);

    /**
     * Удаление задачи по её ID.
     * <p>
     * Этот метод удаляет задачу с заданным идентификатором.
     * </p>
     *
     * @param id идентификатор задачи.
     * @return статус выполнения операции.
     */
    @Operation(summary = "Удалить задачу", description = "Удаляет задачу по переданному идентификатору.")
    ResponseEntity<HttpStatus> deleteTask(@PathVariable Long id);

    /**
     * Получение задачи по её имени.
     * <p>
     * Этот метод возвращает задачу, имеющую указанное имя.
     * </p>
     *
     * @param name имя задачи.
     * @return задача с указанным именем.
     */
    @Operation(summary = "Получить задачу по названию", description = "Возвращает задачу по переданному названию")
    ResponseEntity<Task> getTaskByName(@PathVariable String name);

    /**
     * Назначение задачи пользователю.
     * <p>
     * Этот метод назначает задачу с указанным идентификатором пользователю с заданным ID.
     * </p>
     *
     * @param userId идентификатор пользователя.
     * @param taskId идентификатор задачи.
     * @return статус выполнения операции.
     */
    @Operation(summary = "Назначить задачу пользователю", description = "Связывает задачу с пользователем, назначая исполнителя.")
    ResponseEntity<HttpStatus> assignTaskToUser(@PathVariable Long userId, @PathVariable Long taskId);

}

