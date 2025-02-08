package com.flawden.TaskForgeAPI.config.filter;

import com.flawden.TaskForgeAPI.config.security.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JwtAuthenticationFilter — это кастомная реализация {@link OncePerRequestFilter}, которая перехватывает входящие HTTP-запросы
 * для обработки аутентификации на основе JWT.
 *
 * <p>Этот фильтр извлекает JWT-токен из заголовка "Authorization" каждого запроса, проверяет его и, если токен действителен,
 * устанавливает контекст аутентификации в {@link SecurityContextHolder}.
 *
 * <p>Основные функции:
 * <ul>
 *     <li>Проверка наличия заголовка "Authorization" с токеном "Bearer".</li>
 *     <li>Извлечение и проверка JWT-токена с использованием {@link JwtService}.</li>
 *     <li>Загрузка данных пользователя через {@link UserDetailsService} на основе имени пользователя, извлеченного из токена.</li>
 *     <li>Создание {@link UsernamePasswordAuthenticationToken} и установка его в контекст безопасности.</li>
 * </ul>
 *
 * <p>Если токен недействителен или отсутствует, фильтр продолжает выполнение без установки аутентификации.
 *
 * <p>Этот фильтр должен быть зарегистрирован в цепочке фильтров безопасности.
 *
 * <p>Зависимости:
 * <ul>
 *     <li>{@link JwtService} для извлечения и проверки JWT-токенов.</li>
 *     <li>{@link UserDetailsService} для загрузки данных пользователя на основе имени пользователя из токена.</li>
 * </ul>
 *
 * @see OncePerRequestFilter
 * @see JwtService
 * @see UserDetailsService
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    public void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        final String token;
        final String email;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        token = authHeader.substring(7);
        email = jwtService.extractUsername(token);
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(email);
            if (jwtService.validateToken(token, userDetails)) {
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}
