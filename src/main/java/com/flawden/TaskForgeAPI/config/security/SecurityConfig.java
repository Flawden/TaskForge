package com.flawden.TaskForgeAPI.config.security;

import com.flawden.TaskForgeAPI.config.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Конфигурация безопасности для приложения, основанная на Spring Security.
 *
 * <p>Этот класс конфигурирует правила безопасности приложения, включая настройку фильтров, аутентификацию
 * с использованием JWT и управление сессиями. Он настраивает фильтрацию запросов, чтобы только авторизованные пользователи
 * могли получать доступ к защищенным ресурсам.</p>
 *
 * <p>Основные элементы конфигурации:
 * <ul>
 *     <li>{@link #securityFilterChain(HttpSecurity)} — конфигурация безопасности для HTTP запросов, включая настройки
 *     фильтрации, сессионной политики и провайдеров аутентификации.</li>
 * </ul>
 * </p>
 *
 * @see JwtAuthenticationFilter
 * @see AuthenticationProvider
 * @see HttpSecurity
 * @see SessionCreationPolicy
 */
@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationProvider authenticationProvider;

    /**
     * Конфигурирует фильтры безопасности для HTTP-запросов.
     *
     * <p>Этот метод настраивает политику безопасности для всех HTTP запросов. В частности, он отключает защиту CSRF,
     * разрешает доступ к эндпоинтам аутентификации и настраивает управление сессиями с использованием stateless-сессий.</p>
     *
     * @param http объект {@link HttpSecurity}, используемый для настройки безопасности запросов.
     * @return объект {@link SecurityFilterChain}, содержащий всю конфигурацию безопасности.
     * @throws Exception если возникает ошибка при конфигурации безопасности.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/auth/**", "/swagger-ui/**", "/api-docs/**").permitAll()
                        .anyRequest().authenticated())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

}
