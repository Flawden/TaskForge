package com.flawden.TaskForgeAPI.mapper;

import com.flawden.TaskForgeAPI.dto.task.Priority;
import com.flawden.TaskForgeAPI.dto.task.Status;
import com.flawden.TaskForgeAPI.dto.task.Task;
import com.flawden.TaskForgeAPI.model.TaskEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring", uses = CommentMapper.class)
public interface TaskMapper {

    @Mapping(target = "status", source = "status", qualifiedByName = "enumToString")
    @Mapping(target = "priority", source = "priority", qualifiedByName = "enumToString")
    @Mapping(target = "executors", source = "executors")
    @Mapping(target = "comments", source = "comments")
    Task mapTaskEntityToTask(TaskEntity taskEntity);

    @Mapping(target = "status", source = "status", qualifiedByName = "stringToStatus")
    @Mapping(target = "priority", source = "priority", qualifiedByName = "stringToPriority")
    @Mapping(target = "executors", source = "executors")
    @Mapping(target = "comments", source = "comments")
    TaskEntity mapTaskToTaskEntity(Task task);

    @Named("enumToString")
    static String enumToString(Enum<?> value) {
        return value != null ? value.name() : null;
    }

    @Named("stringToStatus")
    static Status stringToStatus(String value) {
        return value != null ? Status.valueOf(value.toUpperCase()) : null;
    }

    @Named("stringToPriority")
    static Priority stringToPriority(String value) {
        return value != null ? Priority.valueOf(value.toUpperCase()) : null;
    }
}
