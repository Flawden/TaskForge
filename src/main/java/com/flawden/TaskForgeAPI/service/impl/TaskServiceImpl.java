package com.flawden.TaskForgeAPI.service.impl;

import com.flawden.TaskForgeAPI.dto.task.Priority;
import com.flawden.TaskForgeAPI.dto.task.Status;
import com.flawden.TaskForgeAPI.dto.task.Task;
import com.flawden.TaskForgeAPI.exception.TaskNotFoundException;
import com.flawden.TaskForgeAPI.exception.UserAlreadyHaveThisTaskException;
import com.flawden.TaskForgeAPI.exception.UserNotFoundException;
import com.flawden.TaskForgeAPI.mapper.TaskMapper;
import com.flawden.TaskForgeAPI.model.TaskEntity;
import com.flawden.TaskForgeAPI.model.UserEntity;
import com.flawden.TaskForgeAPI.repository.TaskRepository;
import com.flawden.TaskForgeAPI.repository.UserRepository;
import com.flawden.TaskForgeAPI.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Реализация сервиса для работы с задачами.
 * <p>
 * Этот сервис предоставляет методы для получения всех задач, добавления, обновления и удаления задач,
 * а также для получения задач по идентификатору и имени, а также для назначения задач пользователю.
 * </p>
 */
@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final TaskMapper taskMapper;

    /**
     * Получить все задачи.
     * <p>
     * Этот метод возвращает все задачи, извлекая их из репозитория и преобразуя в объекты типа {@link Task}.
     * </p>
     *
     * @return список всех задач.
     */
    @Override
    public List<Task> getAllTasks() {
        return taskRepository.findAll().stream()
                .map(this::mapTaskEntityToTask)
                .collect(Collectors.toList());
    }

    /**
     * Получить задачи с пагинацией.
     * <p>
     * Этот метод возвращает задачи с учетом пагинации, используя параметры страницы и лимита.
     * </p>
     *
     * @param page номер страницы (от 0).
     * @param size количество задач на странице.
     * @return список задач на текущей странице.
     */
    @Override
    public List<Task> getTasksWithPagination(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        return taskRepository.findAll(pageable).stream()
                .map(this::mapTaskEntityToTask)
                .collect(Collectors.toList());
    }

    /**
     * Преобразует {@link TaskEntity} в {@link Task}.
     *
     * @param taskEntity сущность задачи.
     * @return объект задачи {@link Task}.
     */
    private Task mapTaskEntityToTask(TaskEntity taskEntity) {
        return taskMapper.mapTaskEntityToTask(taskEntity);
    }

    /**
     * Получить задачу по идентификатору.
     * <p>
     * Этот метод ищет задачу по ее идентификатору и возвращает ее, если она существует. В противном случае
     * выбрасывается исключение {@link TaskNotFoundException}.
     * </p>
     *
     * @param id идентификатор задачи.
     * @return задача {@link Task}.
     * @throws TaskNotFoundException если задача не найдена.
     */
    @Override
    public Task getTaskById(Long id) {
        return taskMapper.mapTaskEntityToTask(taskRepository.findById(id).orElseThrow(TaskNotFoundException::new));
    }

    /**
     * Добавить новую задачу.
     * <p>
     * Этот метод сохраняет новую задачу в базе данных и возвращает ее.
     * </p>
     *
     * @param task объект задачи для добавления.
     * @return добавленная задача {@link Task}.
     */
    @Override
    public Task addTask(Task task) {
        return taskMapper.mapTaskEntityToTask(taskRepository.save(taskMapper.mapTaskToTaskEntity(task)));
    }

    /**
     * Обновить существующую задачу.
     * <p>
     * Этот метод обновляет задачу с заданным идентификатором. Если задача не найдена, выбрасывается исключение
     * {@link TaskNotFoundException}.
     * </p>
     *
     * @param task   объект задачи с обновленными данными.
     * @param taskId идентификатор задачи для обновления.
     * @throws TaskNotFoundException если задача не найдена.
     */
    @Override
    public void updateTask(Task task, Long taskId) {
        TaskEntity updatableTask = taskRepository.findById(taskId).orElseThrow(TaskNotFoundException::new);
        updatableTask.setTitle(task.getTitle());
        updatableTask.setDescription(task.getDescription());
        updatableTask.setStatus(Status.valueOf(task.getStatus()));
        updatableTask.setPriority(Priority.valueOf(task.getPriority()));
        taskRepository.save(updatableTask);
    }

    /**
     * Удалить задачу по идентификатору.
     * <p>
     * Этот метод удаляет задачу с указанным идентификатором.
     * </p>
     *
     * @param id идентификатор задачи.
     */
    @Override
    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

    /**
     * Получить задачу по имени.
     * <p>
     * Этот метод возвращает задачу с заданным названием. Если задача не найдена, выбрасывается исключение
     * {@link TaskNotFoundException}.
     * </p>
     *
     * @param name название задачи.
     * @return задача {@link Task}.
     * @throws TaskNotFoundException если задача не найдена.
     */
    @Override
    public Task getTaskByName(String name) {
        return taskMapper.mapTaskEntityToTask(taskRepository.findTaskEntitiesByTitle(name).orElseThrow(TaskNotFoundException::new));
    }

    /**
     * Назначить задачу пользователю.
     * <p>
     * Этот метод назначает задачу пользователю с указанным идентификатором. Если задача уже назначена
     * этому пользователю, выбрасывается исключение {@link UserAlreadyHaveThisTaskException}.
     * </p>
     *
     * @param userId идентификатор пользователя.
     * @param taskId идентификатор задачи.
     * @throws TaskNotFoundException            если задача не найдена.
     * @throws UserNotFoundException            если пользователь не найден.
     * @throws UserAlreadyHaveThisTaskException если задача уже назначена пользователю.
     */
    @Override
    public void assignTaskToUser(Long userId, Long taskId) {
        TaskEntity task = taskRepository.findById(taskId).orElseThrow(TaskNotFoundException::new);
        UserEntity user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        for (TaskEntity taskEntity : user.getTaskEntities()) {
            if (taskEntity.getId().equals(taskId)) {
                throw new UserAlreadyHaveThisTaskException();
            }
        }
        user.getTaskEntities().add(task);
        userRepository.save(user);
    }

}
