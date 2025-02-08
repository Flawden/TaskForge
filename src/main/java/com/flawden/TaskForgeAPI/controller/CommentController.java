package com.flawden.TaskForgeAPI.controller;

import com.flawden.TaskForgeAPI.dto.Comment;
import com.flawden.TaskForgeAPI.dto.user.Role;
import com.flawden.TaskForgeAPI.exception.CommentNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Контроллер для работы с комментариями.
 * <p>
 * Этот контроллер предоставляет методы для получения списка комментариев, добавления новых, обновления
 * и удаления существующих комментариев. Также доступны методы для получения комментариев по задаче
 * или пользователю.
 * </p>
 */
@Tag(name = "Комментарии", description = "Управление комментариями")
public interface CommentController {

    /**
     * Получение списка всех комментариев с возможностью пагинации.
     * <p>
     * Этот метод позволяет получить все комментарии с пагинацией. Параметры {@code page} и {@code limit}
     * используются для ограничения количества возвращаемых комментариев.
     * </p>
     *
     * @param page  номер страницы (опционально).
     * @param limit количество комментариев на странице (опционально).
     * @return список комментариев с учетом пагинации.
     */
    @Operation(summary = "Получить все комментарии", description = "Возвращает все комментарии с возможностью пагинации.")
    ResponseEntity<List<Comment>> getAllComments(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "limit", required = false) Integer limit);

    /**
     * Получение комментария по его ID.
     * <p>
     * Этот метод возвращает комментарий с указанным идентификатором.
     * </p>
     *
     * @param id идентификатор комментария.
     * @return комментарий с заданным ID.
     */
    @Operation(summary = "Получить комментарий по идентификатору", description = "Возвращает комментарий по его ID.")
    ResponseEntity<Comment> getCommentById(@PathVariable Long id);

    /**
     * Добавление нового комментария.
     * <p>
     * Этот метод обрабатывает запрос на добавление нового комментария.
     * </p>
     *
     * @param comment объект {@link Comment}, содержащий данные для создания нового комментария.
     * @return добавленный комментарий.
     */
    @Operation(summary = "Добавить новый комментарий", description = "Добавляет новый комментарий в систему.")
    ResponseEntity<Comment> addComment(@RequestBody Comment comment);

    /**
     * Обновление существующего комментария.
     * <p>
     * Этот метод обрабатывает запрос на обновление существующего комментария.
     * </p>
     *
     * @param comment  объект {@link Comment}, содержащий обновленные данные комментария.
     * @param authorId идентификатор пользователя, который является автором комментария.
     * @return статус выполнения операции.
     */
    @Operation(summary = "Обновить комментарий", description = "Обновляет комментарий по его ID.")
    ResponseEntity<HttpStatus> updateComment(@RequestBody Comment comment, Long authorId);

    /**
     * Удалить комментарий.
     * <p>
     * Этот метод удаляет комментарий по его идентификатору.
     * Пользователь может удалить только свои комментарии,
     * или комментарии других пользователей, если он имеет роль {@link Role#ADMIN}.
     * </p>
     *
     * @param id идентификатор комментария для удаления.
     * @param userDetails данные текущего пользователя, выполняющего удаление.
     * @return HTTP статус 200 (OK) в случае успешного удаления комментария.
     * @throws CommentNotFoundException если комментарий с указанным идентификатором не найден.
     * @throws AccessDeniedException если у пользователя нет прав на удаление комментария.
     */
    @Operation(summary = "Удалить комментарий", description = "Удаляет комментарий по его ID.")
    ResponseEntity<HttpStatus> deleteComment(@PathVariable Long id, UserDetails userDetails);

    /**
     * Получение комментария по идентификатору задачи.
     * <p>
     * Этот метод возвращает комментарий, связанный с задачей с заданным идентификатором.
     * </p>
     *
     * @param id идентификатор задачи.
     * @return комментарий, связанный с указанной задачей.
     */
    @Operation(summary = "Получить комментарий по ID задачи", description = "Возвращает комментарий, связанный с конкретной задачей.")
    ResponseEntity<Comment> getCommentByTaskId(@PathVariable Long id);

    /**
     * Получение комментария по идентификатору пользователя.
     * <p>
     * Этот метод возвращает комментарий, созданный пользователем с указанным идентификатором.
     * </p>
     *
     * @param id идентификатор пользователя.
     * @return комментарий, созданный указанным пользователем.
     */
    @Operation(summary = "Получить комментарий по ID пользователя", description = "Возвращает комментарий, связанный с пользователем.")
    ResponseEntity<Comment> getCommentByUserId(@PathVariable Long id);

}
