package com.flawden.TaskForgeAPI.config.security;

import com.flawden.TaskForgeAPI.model.UserEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

/**
 * PersonDetails представляет собой реализацию {@link UserDetails} для пользователя.
 *
 * <p>Этот класс используется для предоставления информации о пользователе, включая его роль,
 * а также настройки, связанные с его учетной записью, такие как состояние блокировки, истечение срока действия и т. д.</p>
 *
 * <p>Основные поля и методы:
 * <ul>
 *     <li>{@link #getAuthorities()} — возвращает список полномочий пользователя.</li>
 *     <li>{@link #getPassword()} — возвращает пароль пользователя.</li>
 *     <li>{@link #getUsername()} — возвращает email пользователя как его имя пользователя.</li>
 *     <li>{@link #isAccountNonExpired()} — возвращает true, если учетная запись не истекла.</li>
 *     <li>{@link #isAccountNonLocked()} — возвращает true, если учетная запись не заблокирована.</li>
 *     <li>{@link #isCredentialsNonExpired()} — возвращает true, если учетные данные пользователя не истекли.</li>
 *     <li>{@link #isEnabled()} — возвращает true, если пользователь включен.</li>
 * </ul>
 * </p>
 *
 * <p>Этот класс обычно используется для передачи информации о пользователе, хранящемся в базе данных,
 * в контексте Spring Security.</p>
 *
 * @see UserDetails
 */
public class PersonDetails implements UserDetails {

    private final UserEntity user;

    public PersonDetails(UserEntity user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(user.getRole().name()));
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
