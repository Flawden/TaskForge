package com.flawden.TaskForgeAPI.mapper;

import com.flawden.TaskForgeAPI.dto.Comment;
import com.flawden.TaskForgeAPI.model.CommentEntity;
import com.flawden.TaskForgeAPI.model.TaskEntity;
import com.flawden.TaskForgeAPI.model.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    CommentMapper INSTANCE = Mappers.getMapper(CommentMapper.class);

    @Mapping(target = "author", source = "author.id")
    @Mapping(target = "task", source = "task.id")
    Comment mapCommentEntityToComment(CommentEntity commentEntity);

    @Mapping(target = "author", source = "author", qualifiedByName = "mapToUserEntity")
    @Mapping(target = "task", source = "task", qualifiedByName = "mapToTaskEntity")
    CommentEntity mapCommentToCommentEntity(Comment comment);

    @Named("mapToUserEntity")
    default UserEntity mapToUserEntity(Long authorId) {
        if (authorId == null) {
            return null;
        }
        UserEntity user = new UserEntity();
        user.setId(authorId);
        return user;
    }

    @Named("mapToTaskEntity")
    default TaskEntity mapToTaskEntity(Long taskId) {
        if (taskId == null) {
            return null;
        }
        TaskEntity task = new TaskEntity();
        task.setId(taskId);
        return task;
    }

}
