package com.flawden.TaskForgeAPI.repository;

import com.flawden.TaskForgeAPI.model.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TaskRepository extends JpaRepository<TaskEntity, Long> {

    Optional<TaskEntity> findTaskEntitiesByTitle(String title);

}
