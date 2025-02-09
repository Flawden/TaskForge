package com.flawden.TaskForgeAPI.mapper;

import com.flawden.TaskForgeAPI.dto.user.Login;
import com.flawden.TaskForgeAPI.dto.user.Register;
import com.flawden.TaskForgeAPI.dto.user.Role;
import com.flawden.TaskForgeAPI.dto.user.User;
import com.flawden.TaskForgeAPI.model.UserEntity;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class UserMapperTest {

    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    @Test
    public void testMapUserEntityToUser() {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setEmail("test@example.com");
        userEntity.setFirstName("John");
        userEntity.setLastName("Doe");
        userEntity.setRole(Role.USER);
        User user = userMapper.mapUserEntityToUser(userEntity);
        assertThat(user).isNotNull();
        assertThat(user.getId()).isEqualTo(1L);
        assertThat(user.getEmail()).isEqualTo("test@example.com");
        assertThat(user.getFirstName()).isEqualTo("John");
        assertThat(user.getLastName()).isEqualTo("Doe");
        assertThat(user.getRole()).isEqualTo("USER");
    }

    @Test
    public void testMapUserToUserEntity() {
        User user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setRole("ADMIN");
        UserEntity userEntity = userMapper.mapUserToUserEntity(user);
        assertThat(userEntity).isNotNull();
        assertThat(userEntity.getId()).isEqualTo(1L);
        assertThat(userEntity.getEmail()).isEqualTo("test@example.com");
        assertThat(userEntity.getFirstName()).isEqualTo("John");
        assertThat(userEntity.getLastName()).isEqualTo("Doe");
        assertThat(userEntity.getRole()).isEqualTo(Role.ADMIN);
    }

    @Test
    public void testMapRegisterToUserEntity() {
        // Подготовка данных
        Register register = new Register();
        register.setUsername("newuser@example.com");
        register.setPassword("securepassword");
        register.setFirstName("Jane");
        register.setLastName("Smith");
        register.setRole("USER");
        UserEntity userEntity = userMapper.mapRegisterToUserEntity(register);
        assertThat(userEntity).isNotNull();
        assertThat(userEntity.getEmail()).isEqualTo("newuser@example.com");
        assertThat(userEntity.getPassword()).isEqualTo("securepassword");
        assertThat(userEntity.getFirstName()).isEqualTo("Jane");
        assertThat(userEntity.getLastName()).isEqualTo("Smith");
        assertThat(userEntity.getRole()).isEqualTo(Role.USER);
    }

    @Test
    public void testMapLoginToUserEntity() {
        Login login = new Login();
        login.setUsername("user@example.com");
        login.setPassword("password123");
        UserEntity userEntity = userMapper.mapLoginToUserEntity(login);
        assertThat(userEntity).isNotNull();
        assertThat(userEntity.getEmail()).isEqualTo("user@example.com");
        assertThat(userEntity.getPassword()).isEqualTo("password123");
    }
}

