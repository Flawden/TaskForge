package com.flawden.TaskForgeAPI.config.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

/**
 * JwtService — это сервис для работы с JWT-токенами, включая их генерацию, валидацию и извлечение информации.
 *
 * <p>Основные функции:
 * <ul>
 *     <li>Генерация JWT-токенов с дополнительными параметрами (например, временем истечения).</li>
 *     <li>Валидация JWT-токенов, проверка их срока действия и соответствия с данными пользователя.</li>
 *     <li>Извлечение информации из токена, такой как имя пользователя и дата истечения.</li>
 * </ul>
 *
 * <p>Основной механизм безопасности реализован через HMAC с использованием секретного ключа, который хранится в конфигурации
 * приложения (с помощью аннотации {@link Value}).
 *
 * <p>Использует библиотеку {@link Jwts} для создания, парсинга и проверки JWT.
 *
 * <p>Методы:
 * <ul>
 *     <li>{@link #generateToken(UserDetails)} — генерирует JWT-токен для пользователя, без дополнительных данных.</li>
 *     <li>{@link #generateToken(Map, UserDetails)} — генерирует JWT-токен с дополнительными данными (например, claims).</li>
 *     <li>{@link #validateToken(String, UserDetails)} — проверяет валидность токена (не истек и соответствует пользователю).</li>
 *     <li>{@link #extractUsername(String)} — извлекает имя пользователя из токена.</li>
 *     <li>{@link #extractAllClaims(String)} — извлекает все данные (claims) из токена.</li>
 * </ul>
 *
 * <p>Конфигурация:
 * <ul>
 *     <li>Секретный ключ для подписи токенов извлекается из конфигурации приложения с использованием {@link Value}.</li>
 * </ul>
 *
 * @see Jwts
 * @see UserDetails
 * @see Claims
 */
@Service
public class JwtService {

    @Value("${secret.key}")
    private String secretKey;

    /**
     * Получает ключ для подписи токенов.
     *
     * @return ключ для подписи токенов.
     */
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    }

    /**
     * Генерирует JWT-токен для пользователя.
     *
     * @param userDetails объект, содержащий информацию о пользователе.
     * @return сгенерированный JWT-токен.
     */
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    /**
     * Генерирует JWT-токен с дополнительными данными.
     *
     * @param extraClaims дополнительные данные для включения в токен.
     * @param userDetails объект, содержащий информацию о пользователе.
     * @return сгенерированный JWT-токен.
     */
    public String generateToken(
            Map<String, Objects> extraClaims,
            UserDetails userDetails
    ) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Проверяет валидность JWT-токена.
     *
     * @param token       JWT-токен для проверки.
     * @param userDetails объект, содержащий информацию о пользователе.
     * @return true, если токен валиден, иначе false.
     */
    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    /**
     * Проверяет, истек ли срок действия JWT-токена.
     *
     * @param token JWT-токен для проверки.
     * @return true, если срок действия токена истек, иначе false.
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Извлекает дату истечения срока действия токена.
     *
     * @param token JWT-токен.
     * @return дата истечения срока действия токена.
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Извлекает имя пользователя из JWT-токена.
     *
     * @param token JWT-токен.
     * @return имя пользователя.
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Извлекает данные (claims) из JWT-токена.
     *
     * @param token          JWT-токен.
     * @param claimsResolver функция для извлечения данных из токена.
     * @param <T>            тип возвращаемого значения.
     * @return данные из токена.
     */
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Извлекает все данные (claims) из JWT-токена.
     *
     * @param token JWT-токен.
     * @return все данные (claims) из токена.
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Получает ключ для подписи токенов.
     *
     * @return ключ для подписи токенов.
     */
    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
