package com.flawden.TaskForgeAPI.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Конфигурация приложения для настройки аутентификации и провайдеров безопасности.
 *
 * <p>Этот класс отвечает за настройку компонентов безопасности, таких как {@link AuthenticationManager},
 * {@link AuthenticationProvider}, а также интеграцию с {@link UserDetailsService} и {@link PasswordEncoder} для обеспечения безопасности приложения.</p>
 *
 * <p>Основные элементы конфигурации:
 * <ul>
 *     <li>{@link #authenticationManager(AuthenticationConfiguration)} — создание и настройка {@link AuthenticationManager}.</li>
 *     <li>{@link #authenticationProvider()} — настройка {@link AuthenticationProvider}, используемого для аутентификации пользователей.</li>
 * </ul>
 * </p>
 *
 * @see PasswordEncoder
 * @see UserDetailsService
 * @see AuthenticationManager
 * @see AuthenticationProvider
 */
@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;

    /**
     * Создает и настраивает {@link AuthenticationManager} для обработки аутентификации.
     *
     * <p>Этот метод использует {@link AuthenticationConfiguration}, предоставляемый Spring Security, для получения
     * экземпляра {@link AuthenticationManager}, который будет использоваться для выполнения аутентификации пользователей
     * с учетом настроек аутентификации и пароля.</p>
     *
     * @param authenticationConfiguration объект конфигурации аутентификации от Spring Security.
     * @return объект {@link AuthenticationManager}, настроенный для использования в приложении.
     * @throws Exception если возникает ошибка при создании {@link AuthenticationManager}.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * Создает {@link AuthenticationProvider} для аутентификации пользователей с использованием {@link UserDetailsService}
     * и {@link PasswordEncoder}.
     *
     * <p>Этот метод создает и настраивает {@link DaoAuthenticationProvider}, который будет использоваться для аутентификации
     * пользователей с их данными из базы данных через {@link UserDetailsService} и проверку пароля с использованием {@link PasswordEncoder}.</p>
     *
     * @return объект {@link AuthenticationProvider}, настроенный для использования в аутентификации.
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
        return daoAuthenticationProvider;
    }

}
