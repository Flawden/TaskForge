package com.flawden.TaskForgeAPI.repository;

import com.flawden.TaskForgeAPI.dto.task.Status;
import com.flawden.TaskForgeAPI.dto.task.Priority;
import com.flawden.TaskForgeAPI.model.TaskEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<TaskEntity, Long> {

    Optional<TaskEntity> findTaskEntitiesByTitle(String title);

    List<TaskEntity> findByTitleContainingAndStatusAndPriority(String title, Status status, Priority priority);

    List<TaskEntity> findByTitleContainingAndStatusAndPriority(String title, Status status, Priority priority, Pageable pageable);

}
