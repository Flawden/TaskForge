package com.flawden.TaskForgeAPI.config.security;

import com.flawden.TaskForgeAPI.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Сервис для загрузки данных о пользователе по имени пользователя (email).
 *
 * <p>Этот сервис реализует интерфейс {@link UserDetailsService} и используется для получения информации
 * о пользователе из базы данных и предоставления этой информации в виде объекта {@link UserDetails}, который
 * используется для аутентификации и авторизации в Spring Security.</p>
 *
 * <p>Основные функции:
 * <ul>
 *     <li>{@link #loadUserByUsername(String)} — находит пользователя по его email и возвращает {@link PersonDetails}.</li>
 *     <li>{@link #passwordEncoder()} — предоставляет экземпляр {@link PasswordEncoder} для работы с паролями.</li>
 * </ul>
 * </p>
 *
 * @see UserDetailsService
 * @see PersonDetails
 * @see PasswordEncoder
 */
@Service
@RequiredArgsConstructor
public class PersonDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Загружает данные о пользователе по его имени пользователя (email).
     *
     * @param username email пользователя, который используется в качестве имени пользователя.
     * @return объект {@link UserDetails}, содержащий данные о пользователе.
     * @throws UsernameNotFoundException если пользователь с таким email не найден.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return new PersonDetails(userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("Bad credentional")));
    }

    /**
     * Создает и возвращает {@link PasswordEncoder} для кодирования паролей.
     *
     * <p>Используется для хеширования паролей, например, при регистрации или смене пароля.</p>
     *
     * @return экземпляр {@link BCryptPasswordEncoder} с параметром сложности 16.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(16);
    }
}
