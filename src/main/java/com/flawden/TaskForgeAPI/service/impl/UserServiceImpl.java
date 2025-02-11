package com.flawden.TaskForgeAPI.service.impl;

import com.flawden.TaskForgeAPI.dto.user.UpdateUser;
import com.flawden.TaskForgeAPI.dto.user.User;
import com.flawden.TaskForgeAPI.exception.UserNotFoundException;
import com.flawden.TaskForgeAPI.mapper.UserMapper;
import com.flawden.TaskForgeAPI.model.UserEntity;
import com.flawden.TaskForgeAPI.repository.UserRepository;
import com.flawden.TaskForgeAPI.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Реализация сервиса для работы с пользователями.
 * <p>
 * Этот сервис предоставляет методы для получения всех пользователей, добавления, обновления и удаления пользователей,
 * а также для получения пользователя по идентификатору и имени (username).
 * </p>
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    /**
     * Получить всех пользователей.
     * <p>
     * Этот метод возвращает всех пользователей из репозитория, преобразованных в объекты типа {@link User}.
     * </p>
     *
     * @return список всех пользователей.
     */
    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::mapUserEntityToUser)
                .collect(Collectors.toList());
    }

    /**
     * Получить пользователей с пагинацией.
     * <p>
     * Этот метод возвращает пользователей с учетом пагинации.
     * </p>
     *
     * @param page номер страницы (от 0).
     * @param size количество пользователей на странице.
     * @return список пользователей на текущей странице.
     */
    @Override
    public List<User> getUsersWithPagination(Integer page, Integer size) {
        return userRepository.findAll().stream()
                .map(this::mapUserEntityToUser)
                .collect(Collectors.toList());
    }

    /**
     * Преобразует {@link UserEntity} в {@link User}.
     *
     * @param userEntity сущность пользователя.
     * @return объект пользователя {@link User}.
     */
    private User mapUserEntityToUser(UserEntity userEntity) {
        return userMapper.mapUserEntityToUser(userEntity);
    }

    /**
     * Получить пользователя по идентификатору.
     * <p>
     * Этот метод ищет пользователя по его идентификатору и возвращает его, если он существует. В противном случае
     * выбрасывается исключение {@link UserNotFoundException}.
     * </p>
     *
     * @param id идентификатор пользователя.
     * @return пользователь {@link User}.
     * @throws UserNotFoundException если пользователь не найден.
     */
    @Override
    public User getUserById(Long id) {
        return userMapper.mapUserEntityToUser(userRepository.findById(id).orElseThrow(UserNotFoundException::new));
    }

    /**
     * Добавить нового пользователя.
     * <p>
     * Этот метод сохраняет нового пользователя в базе данных и возвращает его.
     * </p>
     *
     * @param user объект пользователя для добавления.
     * @return добавленный пользователь {@link User}.
     */
    @Override
    @Transactional
    public User addUser(User user) {
        return userMapper.mapUserEntityToUser(userRepository.save(userMapper.mapUserToUserEntity(user)));
    }

    /**
     * Обновить существующего пользователя.
     * <p>
     * Этот метод обновляет пользователя с заданным идентификатором. Если пользователь не найден, выбрасывается исключение
     * {@link UserNotFoundException}.
     * </p>
     *
     * @param user   объект пользователя с обновленными данными.
     * @param userId идентификатор пользователя для обновления.
     * @throws UserNotFoundException если пользователь не найден.
     */
    @Override
    @Transactional
    public void updateUser(UpdateUser user, Long userId) {
        UserEntity updatableUser = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        updatableUser.setEmail(user.getEmail());
        updatableUser.setFirstName(user.getFirstName());
        updatableUser.setLastName(user.getLastName());
        userRepository.save(updatableUser);
    }

    /**
     * Удалить пользователя по идентификатору.
     * <p>
     * Этот метод удаляет пользователя с указанным идентификатором.
     * </p>
     *
     * @param id идентификатор пользователя.
     */
    @Override
    @Transactional
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    /**
     * Получить пользователя по имени (username).
     * <p>
     * Этот метод ищет пользователя по его email (который используется как username) и возвращает его, если он существует.
     * Если пользователь не найден, выбрасывается исключение {@link UserNotFoundException}.
     * </p>
     *
     * @param username email пользователя.
     * @return пользователь {@link User}.
     * @throws UserNotFoundException если пользователь не найден.
     */
    @Override
    public User getUserByUsername(String username) {
        return userMapper.mapUserEntityToUser(userRepository.findByEmail(username).orElseThrow(UserNotFoundException::new));
    }
}
