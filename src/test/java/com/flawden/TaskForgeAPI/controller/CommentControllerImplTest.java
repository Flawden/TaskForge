package com.flawden.TaskForgeAPI.controller;

import com.flawden.TaskForgeAPI.dto.Comment;
import com.flawden.TaskForgeAPI.dto.user.Role;
import com.flawden.TaskForgeAPI.exception.CommentNotFoundException;
import com.flawden.TaskForgeAPI.mapper.CommentMapper;
import com.flawden.TaskForgeAPI.model.CommentEntity;
import com.flawden.TaskForgeAPI.model.UserEntity;
import com.flawden.TaskForgeAPI.repository.CommentRepository;
import com.flawden.TaskForgeAPI.repository.UserRepository;
import com.flawden.TaskForgeAPI.service.impl.CommentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CommentControllerImplTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private CommentMapper commentMapper;

    @Mock
    private UserRepository userRepository;

    private CommentServiceImpl commentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        commentService = new CommentServiceImpl(commentRepository, userRepository, commentMapper);
    }

    @Test
    void testGetAllComments() {
        CommentEntity commentEntity1 = new CommentEntity(1L, "comment 1", null, null);
        CommentEntity commentEntity2 = new CommentEntity(2L, "comment 2", null, null);
        Comment comment1 = new Comment(1L, "comment 1", null, null);
        Comment comment2 = new Comment(2L, "comment 2", null, null);
        when(commentRepository.findAll()).thenReturn(Arrays.asList(commentEntity1, commentEntity2));
        when(commentMapper.mapCommentEntityToComment(commentEntity1)).thenReturn(comment1);
        when(commentMapper.mapCommentEntityToComment(commentEntity2)).thenReturn(comment2);
        var result = commentService.getAllComments();
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("comment 1", result.get(0).getText());
        assertEquals("comment 2", result.get(1).getText());
    }

    @Test
    void testGetCommentsWithPagination() {
        CommentEntity commentEntity = new CommentEntity(1L, "comment", null, null);
        Comment comment = new Comment(1L, "comment", null, null);
        Page<CommentEntity> page = new PageImpl<>(Arrays.asList(commentEntity), PageRequest.of(0, 1), 1);
        when(commentRepository.findAll(PageRequest.of(0, 1))).thenReturn(page);
        when(commentMapper.mapCommentEntityToComment(commentEntity)).thenReturn(comment);
        var result = commentService.getCommentsWithPagination(0, 1);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("comment", result.get(0).getText());
    }

    @Test
    void testGetCommentByIdSuccess() {
        Long commentId = 1L;
        CommentEntity commentEntity = new CommentEntity(1L, "comment", null, null);
        Comment comment = new Comment(commentId, "comment", null, null);
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(commentEntity));
        when(commentMapper.mapCommentEntityToComment(commentEntity)).thenReturn(comment);
        var result = commentService.getCommentById(commentId);
        assertNotNull(result);
        assertEquals(commentId, result.getId());
        assertEquals("comment", result.getText());
    }

    @Test
    void testGetCommentByIdNotFound() {
        Long commentId = 1L;
        when(commentRepository.findById(commentId)).thenReturn(Optional.empty());
        CommentNotFoundException exception = assertThrows(CommentNotFoundException.class, () -> commentService.getCommentById(commentId));
        assertNotNull(exception);
    }

    @Test
    void testAddComment() {
        Comment comment = new Comment(1L, "new comment", null, null);
        CommentEntity commentEntity = new CommentEntity(1L, "new comment", null, null);
        when(commentMapper.mapCommentToCommentEntity(comment)).thenReturn(commentEntity);
        when(commentRepository.save(commentEntity)).thenReturn(commentEntity);
        when(commentMapper.mapCommentEntityToComment(commentEntity)).thenReturn(comment);
        var result = commentService.addComment(comment);
        assertNotNull(result);
        assertEquals("new comment", result.getText());
    }

    @Test
    void testUpdateCommentSuccess() {
        Long authorId = 1L;
        Long userId = 1L;
        String email = "testEmail2222@gmail.com";
        UserEntity user = new UserEntity(userId, email, "password", "firstname", "lastname", Role.USER, null);
        CommentEntity commentEntity = new CommentEntity(1L, "new comment", user, null);
        Comment comment = new Comment(1L, "updated comment", null, null);
        CommentEntity existingComment = new CommentEntity(1L, "existing comment", user, null);
        when(commentRepository.findById(1L)).thenReturn(Optional.of(commentEntity));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(commentRepository.save(existingComment)).thenReturn(existingComment);
        commentService.updateComment(comment, authorId);
        assertEquals("existing comment", existingComment.getText());
    }

    @Test
    void testUpdateCommentNotFound() {
        Long authorId = 1L;
        Comment comment = new Comment(1L, "updated comment", null, null);
        when(commentRepository.findCommentEntityByAuthorId(authorId)).thenReturn(Optional.empty());
        CommentNotFoundException exception = assertThrows(CommentNotFoundException.class, () -> commentService.updateComment(comment, authorId));
        assertNotNull(exception);
    }

    @Test
    void testDeleteComment() {
        Long commentId = 1L;
        Long userId = 1L;
        String email = "testEmail2222@gmail.com";
        UserEntity user = new UserEntity(userId, email, "password", "firstname", "lastname", Role.USER, null);
        CommentEntity commentEntity = new CommentEntity(commentId, "new comment", user, null);
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(commentEntity));
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        commentService.deleteComment(commentId, email);
        verify(commentRepository, times(1)).deleteById(commentId);
    }

    @Test
    void testGetCommentByTaskIdSuccess() {
        Long taskId = 1L;
        CommentEntity commentEntity = new CommentEntity(1L, "comment for task", null, null);
        Comment comment = new Comment(1L, "comment for task", null, null);
        when(commentRepository.findCommentEntitiesByTaskId(taskId)).thenReturn(Optional.of(commentEntity));
        when(commentMapper.mapCommentEntityToComment(commentEntity)).thenReturn(comment);
        var result = commentService.getCommentByTaskId(taskId);
        assertNotNull(result);
        assertEquals("comment for task", result.getText());
    }

    @Test
    void testGetCommentByTaskIdNotFound() {
        Long taskId = 1L;
        when(commentRepository.findCommentEntitiesByTaskId(taskId)).thenReturn(Optional.empty());
        CommentNotFoundException exception = assertThrows(CommentNotFoundException.class, () -> commentService.getCommentByTaskId(taskId));
        assertNotNull(exception);
    }

    @Test
    void testGetCommentByUserIdSuccess() {
        Long userId = 1L;
        CommentEntity commentEntity = new CommentEntity(1L, "comment for user", null, null);
        Comment comment = new Comment(1L, "comment for user", null, null);
        when(commentRepository.findCommentEntityByAuthorId(userId)).thenReturn(Optional.of(commentEntity));
        when(commentMapper.mapCommentEntityToComment(commentEntity)).thenReturn(comment);
        var result = commentService.getCommentByUserId(userId);
        assertNotNull(result);
        assertEquals("comment for user", result.getText());
    }

    @Test
    void testGetCommentByUserIdNotFound() {
        Long userId = 1L;
        when(commentRepository.findCommentEntityByAuthorId(userId)).thenReturn(Optional.empty());
        CommentNotFoundException exception = assertThrows(CommentNotFoundException.class, () -> commentService.getCommentByUserId(userId));
        assertNotNull(exception);
    }
}
