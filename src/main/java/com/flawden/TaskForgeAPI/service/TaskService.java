package com.flawden.TaskForgeAPI.service;

import com.flawden.TaskForgeAPI.dto.task.Task;
import com.flawden.TaskForgeAPI.exception.TaskNotFoundException;
import com.flawden.TaskForgeAPI.exception.UserAlreadyHaveThisTaskException;
import com.flawden.TaskForgeAPI.exception.UserNotFoundException;

import java.util.List;

/**
 * Сервис для работы с задачами.
 * <p>
 * Этот сервис предоставляет методы для управления задачами, включая получение всех задач, создание, обновление,
 * удаление и поиск задач по различным параметрам, а также привязку задач к пользователю.
 * </p>
 */
public interface TaskService {

    /**
     * Получение всех задач.
     * <p>
     * Этот метод возвращает список всех задач в системе.
     * </p>
     *
     * @return список всех задач.
     */
    List<Task> getAllTasks();

    /**
     * Получение задач с пагинацией.
     * <p>
     * Этот метод возвращает список задач с возможностью пагинации.
     * </p>
     *
     * @param page номер страницы.
     * @param size количество задач на странице.
     * @return список задач на указанной странице.
     */
    List<Task> getTasksWithPagination(Integer page, Integer size);

    /**
     * Получение задачи по ее идентификатору.
     * <p>
     * Этот метод возвращает задачу по заданному идентификатору.
     * </p>
     *
     * @param id идентификатор задачи.
     * @return задача с указанным идентификатором.
     * @throws TaskNotFoundException если задача с таким идентификатором не найдена.
     */
    Task getTaskById(Long id);

    /**
     * Добавление новой задачи.
     * <p>
     * Этот метод позволяет добавить новую задачу в систему.
     * </p>
     *
     * @param task объект задачи, который нужно добавить.
     * @return добавленная задача.
     */
    Task addTask(Task task);

    /**
     * Обновление задачи.
     * <p>
     * Этот метод позволяет обновить существующую задачу. Обновление возможно только для
     * задачи с указанным идентификатором.
     * </p>
     *
     * @param task   объект задачи с новыми данными.
     * @param taskId идентификатор задачи, которую нужно обновить.
     * @throws TaskNotFoundException если задача с таким идентификатором не найдена.
     */
    void updateTask(Task task, Long taskId);

    /**
     * Удаление задачи.
     * <p>
     * Этот метод позволяет удалить задачу по ее идентификатору.
     * </p>
     *
     * @param id идентификатор задачи, которую нужно удалить.
     * @throws TaskNotFoundException если задача с таким идентификатором не найдена.
     */
    void deleteTask(Long id);

    /**
     * Получение задачи по имени.
     * <p>
     * Этот метод возвращает задачу с указанным названием.
     * </p>
     *
     * @param name имя задачи.
     * @return задача с указанным именем.
     * @throws TaskNotFoundException если задача с таким именем не найдена.
     */
    Task getTaskByName(String name);

    /**
     * Привязка задачи к пользователю.
     * <p>
     * Этот метод позволяет привязать задачу к пользователю по их идентификаторам.
     * </p>
     *
     * @param userId идентификатор пользователя.
     * @param taskId идентификатор задачи.
     * @throws TaskNotFoundException            если задача с таким идентификатором не найдена.
     * @throws UserNotFoundException            если пользователь с таким идентификатором не найден.
     * @throws UserAlreadyHaveThisTaskException если пользователь уже имеет эту задачу.
     */
    void assignTaskToUser(Long userId, Long taskId);

}
