package com.flawden.TaskForgeAPI.controller.impl;

import com.flawden.TaskForgeAPI.controller.CommentController;
import com.flawden.TaskForgeAPI.dto.Comment;
import com.flawden.TaskForgeAPI.dto.user.Role;
import com.flawden.TaskForgeAPI.exception.CommentNotFoundException;
import com.flawden.TaskForgeAPI.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Контроллер для работы с комментариями.
 * <p>
 * Этот контроллер предоставляет функционал для создания, получения, обновления и удаления комментариев.
 * </p>
 */
@RestController
@RequestMapping("/api/v1/comments")
@RequiredArgsConstructor
public class CommentControllerImpl implements CommentController {

    private final CommentService commentService;

    /**
     * Получить все комментарии с возможностью пагинации.
     * <p>
     * Если параметры пагинации не переданы, возвращаются все комментарии. В противном случае, данные
     * комментарии возвращаются с учетом заданных значений страницы и размера страницы.
     * </p>
     *
     * @param page номер страницы (необязательный параметр).
     * @param size количество комментариев на одной странице (необязательный параметр).
     * @return список комментариев.
     */
    @GetMapping
    @Override
    public ResponseEntity<List<Comment>> getAllComments(Integer page, Integer size) {
        if (page == null) {
            return ResponseEntity.ok(commentService.getAllComments());
        } else {
            return ResponseEntity.ok(commentService.getCommentsWithPagination(page, size));
        }
    }

    /**
     * Получить комментарий по идентификатору.
     * <p>
     * Возвращает комментарий, соответствующий переданному идентификатору.
     * </p>
     *
     * @param id идентификатор комментария.
     * @return комментарий с данным идентификатором.
     */
    @GetMapping("/{id}")
    @Override
    public ResponseEntity<Comment> getCommentById(Long id) {
        return ResponseEntity.ok(commentService.getCommentById(id));
    }

    /**
     * Добавить новый комментарий.
     * <p>
     * Добавляет новый комментарий в систему.
     * </p>
     *
     * @param comment объект комментария, который нужно добавить.
     * @return добавленный комментарий.
     */
    @PostMapping
    @Override
    public ResponseEntity<Comment> addComment(@Valid Comment comment) {
        return ResponseEntity.ok(commentService.addComment(comment));
    }

    /**
     * Обновить комментарий.
     * <p>
     * Обновляет комментарий с указанным идентификатором, если автор совпадает с данным в параметре.
     * </p>
     *
     * @param comment  обновленные данные комментария.
     * @param authorId идентификатор автора комментария.
     * @return HTTP статус успешного обновления.
     */
    @PatchMapping("/{id}")
    @Override
    public ResponseEntity<HttpStatus> updateComment(@Valid Comment comment, Long authorId) {
        commentService.updateComment(comment, authorId);
        return ResponseEntity.ok().build();
    }

    /**
     * Удалить комментарий.
     * <p>
     * Этот метод удаляет комментарий по его идентификатору.
     * Пользователь может удалить только свои комментарии,
     * или комментарии других пользователей, если он имеет роль {@link Role#ADMIN}.
     * </p>
     *
     * @param id          идентификатор комментария для удаления.
     * @param userDetails данные текущего пользователя, выполняющего удаление.
     * @return HTTP статус 200 (OK) в случае успешного удаления комментария.
     * @throws CommentNotFoundException если комментарий с указанным идентификатором не найден.
     * @throws AccessDeniedException    если у пользователя нет прав на удаление комментария.
     */
    @DeleteMapping("/{id}")
    @Override
    public ResponseEntity<HttpStatus> deleteComment(Long id, UserDetails userDetails) {
        commentService.deleteComment(id, userDetails.getUsername());
        return ResponseEntity.ok().build();
    }

    /**
     * Получить комментарий по идентификатору задачи.
     * <p>
     * Возвращает комментарий, связанный с конкретной задачей, если таковой имеется.
     * </p>
     *
     * @param id идентификатор задачи.
     * @return комментарий, привязанный к задаче.
     */
    @GetMapping("/task/{id}")
    @Override
    public ResponseEntity<Comment> getCommentByTaskId(Long id) {
        return ResponseEntity.ok(commentService.getCommentByTaskId(id));
    }

    /**
     * Получить комментарий по идентификатору пользователя.
     * <p>
     * Возвращает комментарий, связанный с конкретным пользователем.
     * </p>
     *
     * @param id идентификатор пользователя.
     * @return комментарий, принадлежащий пользователю.
     */
    @GetMapping("/user/{id}")
    @Override
    public ResponseEntity<Comment> getCommentByUserId(Long id) {
        return ResponseEntity.ok(commentService.getCommentByUserId(id));
    }

    /**
     * Обработчик исключения, когда комментарий не найден.
     * <p>
     * Возвращает ошибку с кодом 404, если комментарий не найден в базе данных.
     * </p>
     *
     * @param e исключение типа {@link CommentNotFoundException}, которое возникает, если комментарий не найден.
     * @return сообщение об ошибке с соответствующим статусом 404.
     */
    @ExceptionHandler(CommentNotFoundException.class)
    public ResponseEntity<String> handleCommentNotFoundException(CommentNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
}
