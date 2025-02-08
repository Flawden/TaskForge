package com.flawden.TaskForgeAPI.service.impl;

import com.flawden.TaskForgeAPI.dto.Comment;
import com.flawden.TaskForgeAPI.dto.user.Role;
import com.flawden.TaskForgeAPI.exception.CommentNotFoundException;
import com.flawden.TaskForgeAPI.exception.UserNotFoundException;
import com.flawden.TaskForgeAPI.mapper.CommentMapper;
import com.flawden.TaskForgeAPI.model.CommentEntity;
import com.flawden.TaskForgeAPI.model.UserEntity;
import com.flawden.TaskForgeAPI.repository.CommentRepository;
import com.flawden.TaskForgeAPI.repository.UserRepository;
import com.flawden.TaskForgeAPI.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Реализация сервиса для работы с комментариями.
 * <p>
 * Этот сервис предоставляет методы для получения всех комментариев, добавления, обновления и удаления комментариев,
 * а также для получения комментариев по идентификатору задачи или пользователя.
 * </p>
 */
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final CommentMapper commentMapper;

    /**
     * Получить все комментарии.
     * <p>
     * Этот метод возвращает все комментарии, извлекая их из репозитория и преобразуя в объекты типа {@link Comment}.
     * </p>
     *
     * @return список всех комментариев.
     */
    @Override
    public List<Comment> getAllComments() {
        return commentRepository.findAll().stream()
                .map(this::mapCommentEntityToComment)
                .collect(Collectors.toList());
    }

    /**
     * Получить комментарии с пагинацией.
     * <p>
     * Этот метод возвращает комментарии с учетом пагинации, используя параметры страницы и лимита.
     * </p>
     *
     * @param page  номер страницы (от 0).
     * @param limit количество комментариев на странице.
     * @return список комментариев на текущей странице.
     */
    @Override
    public List<Comment> getCommentsWithPagination(Integer page, Integer limit) {
        Pageable pageable = PageRequest.of(page, limit);
        return commentRepository.findAll(pageable)
                .stream()
                .map(this::mapCommentEntityToComment)
                .collect(Collectors.toList());
    }

    /**
     * Преобразует {@link CommentEntity} в {@link Comment}.
     *
     * @param commentEntity сущность комментария.
     * @return объект комментария {@link Comment}.
     */
    private Comment mapCommentEntityToComment(CommentEntity commentEntity) {
        return commentMapper.mapCommentEntityToComment(commentEntity);
    }

    /**
     * Получить комментарий по идентификатору.
     * <p>
     * Этот метод ищет комментарий по его идентификатору и возвращает его, если он существует. В противном случае
     * выбрасывается исключение {@link CommentNotFoundException}.
     * </p>
     *
     * @param id идентификатор комментария.
     * @return комментарий {@link Comment}.
     * @throws CommentNotFoundException если комментарий не найден.
     */
    @Override
    public Comment getCommentById(Long id) {
        return commentMapper.mapCommentEntityToComment(commentRepository.findById(id).orElseThrow(CommentNotFoundException::new));
    }

    /**
     * Добавить новый комментарий.
     * <p>
     * Этот метод сохраняет новый комментарий в базе данных.
     * </p>
     *
     * @param comment объект комментария для добавления.
     * @return добавленный комментарий {@link Comment}.
     */
    @Override
    @Transactional
    public Comment addComment(Comment comment) {
        return commentMapper.mapCommentEntityToComment(commentRepository.save(commentMapper.mapCommentToCommentEntity(comment)));
    }

    /**
     * Обновить существующий комментарий.
     * <p>
     * Этот метод позволяет обновить комментарий с заданным идентификатором.
     * Пользователь с ролью {@link Role#ADMIN} может редактировать любые комментарии и изменять автора комментария.
     * Пользователь с ролью {@link Role#USER} может редактировать только свои комментарии.
     * Если комментарий не найден, выбрасывается исключение {@link CommentNotFoundException}.
     * Если у пользователя недостаточно прав для редактирования, выбрасывается {@link AccessDeniedException}.
     * </p>
     *
     * @param comment  объект комментария с обновленными данными (поле {@code author} содержит идентификатор нового автора).
     * @param authorId идентификатор текущего пользователя, выполняющего запрос.
     * @throws CommentNotFoundException если комментарий не найден.
     * @throws AccessDeniedException    если у пользователя недостаточно прав для редактирования.
     * @throws UserNotFoundException    если указанный автор не найден (в случае изменения автора комментария).
     */
    @Override
    @Transactional
    public void updateComment(Comment comment, Long authorId) {
        CommentEntity updatableComment = commentRepository.findById(comment.getId())
                .orElseThrow(CommentNotFoundException::new);
        UserEntity currentUser = userRepository.findById(authorId)
                .orElseThrow(UserNotFoundException::new);
        if (currentUser.getRole().equals(Role.ADMIN)) {
            updateCommentDetails(updatableComment, comment);
        } else {
            if (!updatableComment.getAuthor().getId().equals(authorId)) {
                throw new AccessDeniedException("У вас нет прав для редактирования этого комментария");
            }
            updateCommentDetails(updatableComment, comment);
        }
    }

    /**
     * Обновить детали комментария.
     * <p>
     * Этот метод обновляет детали комментария, такие как текст.
     * Автор комментария не может быть изменен обычным пользователем.
     * Если были внесены изменения, комментарий сохраняется в базе данных.
     * </p>
     *
     * @param updatableComment сущность комментария, который нужно обновить.
     * @param comment          объект комментария с новыми данными.
     * @throws AccessDeniedException если попытка изменить автора комментария пользователем.
     */
    private void updateCommentDetails(CommentEntity updatableComment, Comment comment) {
        boolean isSomethingChanged = false;
        if (comment.getAuthor() != null) {
            throw new AccessDeniedException("Только администратор может менять автора комментария");
        }
        if (comment.getText() != null) {
            updatableComment.setText(comment.getText());
            isSomethingChanged = true;
        }
        if (isSomethingChanged) {
            commentRepository.save(updatableComment);
        }
    }


    /**
     * Удаление комментария по идентификатору.
     * <p>
     * Этот метод удаляет комментарий с указанным идентификатором.
     * Пользователь может удалить только свои комментарии.
     * Если пользователь не является автором комментария, он может удалить комментарий только в случае,
     * если у него роль {@link Role#ADMIN}.
     * </p>
     *
     * @param id идентификатор комментария, который нужно удалить.
     * @param email электронная почта текущего пользователя, выполняющего запрос.
     * @throws AccessDeniedException если у пользователя нет прав для удаления комментария.
     * @throws CommentNotFoundException если комментарий с указанным идентификатором не найден.
     * @throws UserNotFoundException если пользователь с указанной электронной почтой не найден.
     */
    @Override
    @Transactional
    public void deleteComment(Long id, String email) {
        CommentEntity commentEntity = commentRepository.findById(id)
                .orElseThrow(CommentNotFoundException::new);
        UserEntity currentUser = userRepository.findByEmail(email)
                .orElseThrow(UserNotFoundException::new);
        if (!currentUser.getRole().equals(Role.ADMIN) && !commentEntity.getAuthor().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("У вас нет прав для удаления этого комментария");
        }
        commentRepository.deleteById(id);
    }


    /**
     * Получить комментарий по идентификатору задачи.
     * <p>
     * Этот метод возвращает комментарий, привязанный к задаче с указанным идентификатором.
     * </p>
     *
     * @param id идентификатор задачи.
     * @return комментарий {@link Comment}.
     * @throws CommentNotFoundException если комментарий не найден.
     */
    @Override
    public Comment getCommentByTaskId(Long id) {
        return commentMapper.mapCommentEntityToComment(commentRepository.findCommentEntitiesByTaskId(id).orElseThrow(CommentNotFoundException::new));
    }

    /**
     * Получить комментарий по идентификатору пользователя.
     * <p>
     * Этот метод возвращает комментарий, созданный пользователем с указанным идентификатором.
     * </p>
     *
     * @param id идентификатор пользователя.
     * @return комментарий {@link Comment}.
     * @throws CommentNotFoundException если комментарий не найден.
     */
    @Override
    public Comment getCommentByUserId(Long id) {
        return commentMapper.mapCommentEntityToComment(commentRepository.findCommentEntityByAuthorId(id).orElseThrow(CommentNotFoundException::new));
    }
}
