package com.flawden.TaskForgeAPI.controller.impl;

import com.flawden.TaskForgeAPI.controller.TaskController;
import com.flawden.TaskForgeAPI.dto.task.Priority;
import com.flawden.TaskForgeAPI.dto.task.Status;
import com.flawden.TaskForgeAPI.dto.task.Task;
import com.flawden.TaskForgeAPI.exception.TaskNotFoundException;
import com.flawden.TaskForgeAPI.exception.UserAlreadyHaveThisTaskException;
import com.flawden.TaskForgeAPI.exception.UserNotFoundException;
import com.flawden.TaskForgeAPI.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Контроллер для работы с задачами.
 * <p>
 * Этот контроллер предоставляет функционал для создания, получения, обновления, удаления задач,
 * а также для назначения задач пользователю.
 * </p>
 */
@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
public class TaskControllerImpl implements TaskController {

    private final TaskService taskService;

    /**
     * Получить все задачи с возможностью пагинации.
     * <p>
     * Если параметры пагинации не переданы, возвращаются все задачи. В противном случае, данные
     * задачи возвращаются с учетом заданных значений страницы и размера страницы.
     * </p>
     *
     * @param page  номер страницы (необязательный параметр).
     * @param limit количество задач на одной странице (необязательный параметр).
     * @return список задач.
     */
    @GetMapping
    @Override
    public ResponseEntity<List<Task>> getAllTasks(Integer page, Integer limit, String title, Status status, Priority priority) {
        if (page == null && limit == null && title == null && status == null && priority == null) {
            return ResponseEntity.ok(taskService.getAllTasks());
        } if (page != null && limit != null) {
            return ResponseEntity.ok(taskService.getTasksWithPagination(page, limit));
        }  if (title != null || status != null || priority != null) {
            return ResponseEntity.ok(taskService.getTasksWithFiltration(title, status, priority));
        } else {
            return ResponseEntity.ok(taskService.getTasksWithPaginationAndFiltration(page, limit, title, status, priority));
        }
    }

    /**
     * Получить задачу по идентификатору.
     * <p>
     * Возвращает задачу, соответствующую переданному идентификатору.
     * </p>
     *
     * @param id идентификатор задачи.
     * @return задача с данным идентификатором.
     */
    @GetMapping("/{id}")
    @Override
    public ResponseEntity<Task> getTaskById(Long id) {
        return ResponseEntity.ok(taskService.getTaskById(id));
    }

    /**
     * Добавить новую задачу.
     * <p>
     * Добавляет новую задачу в систему.
     * </p>
     *
     * @param task объект задачи, который нужно добавить.
     * @return добавленная задача.
     */
    @PostMapping
    @Override
    public ResponseEntity<Task> addTask(@Valid Task task) {
        return ResponseEntity.ok(taskService.addTask(task));
    }

    /**
     * Обновить задачу.
     * <p>
     * Обновляет задачу с указанным идентификатором.
     * </p>
     *
     * @param task   обновленные данные задачи.
     * @param taskId идентификатор задачи, которую нужно обновить.
     * @return HTTP статус успешного обновления.
     */
    @PatchMapping("/{id}")
    @Override
    public ResponseEntity<HttpStatus> updateTask(@Valid Task task, Long taskId) {
        taskService.updateTask(task, taskId);
        return ResponseEntity.ok().build();
    }

    /**
     * Удалить задачу.
     * <p>
     * Удаляет задачу по идентификатору.
     * </p>
     *
     * @param id идентификатор задачи для удаления.
     * @return HTTP статус успешного удаления.
     */
    @DeleteMapping("/{id}")
    @Override
    public ResponseEntity<HttpStatus> deleteTask(Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.ok().build();
    }

    /**
     * Получить задачу по имени.
     * <p>
     * Возвращает задачу, которая соответствует имени, переданному в запросе.
     * </p>
     *
     * @param name имя задачи.
     * @return задача с данным именем.
     */
    @GetMapping("/user/{username}")
    @Override
    public ResponseEntity<Task> getTaskByName(String name) {
        return ResponseEntity.ok(taskService.getTaskByName(name));
    }

    /**
     * Назначить задачу пользователю.
     * <p>
     * Связывает задачу с пользователем, назначая ей исполнителя.
     * </p>
     *
     * @param userId идентификатор пользователя.
     * @param taskId идентификатор задачи.
     * @return HTTP статус успешного назначения задачи.
     */
    @PostMapping("/{userId}/{taskId}")
    @Override
    public ResponseEntity<HttpStatus> assignTaskToUser(Long userId, Long taskId) {
        taskService.assignTaskToUser(userId, taskId);
        return ResponseEntity.ok().build();
    }

    /**
     * Обработчик исключения, когда пользователь уже имеет эту задачу.
     * <p>
     * Возвращает ошибку с кодом 409, если пользователь уже назначен на данную задачу.
     * </p>
     *
     * @param e исключение типа {@link UserAlreadyHaveThisTaskException}, которое возникает, если задача уже назначена пользователю.
     * @return сообщение об ошибке с соответствующим статусом 409.
     */
    @ExceptionHandler(UserAlreadyHaveThisTaskException.class)
    public ResponseEntity<String> handleUserAlreadyHaveThisTaskException(UserAlreadyHaveThisTaskException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }

    /**
     * Обработчик исключения, когда задача не найдена.
     * <p>
     * Возвращает ошибку с кодом 404, если задача с данным идентификатором не найдена.
     * </p>
     *
     * @param e исключение типа {@link TaskNotFoundException}, которое возникает, если задача не найдена.
     * @return сообщение об ошибке с соответствующим статусом 404.
     */
    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<String> handleTaskNotFoundException(TaskNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    /**
     * Обработчик исключения, когда пользователь не найден.
     * <p>
     * Возвращает ошибку с кодом 404, если пользователь не найден в системе.
     * </p>
     *
     * @param e исключение типа {@link UserNotFoundException}, которое возникает, если пользователь не найден.
     * @return сообщение об ошибке с соответствующим статусом 404.
     */
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

}
