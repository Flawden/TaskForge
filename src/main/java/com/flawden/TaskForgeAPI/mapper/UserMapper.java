package com.flawden.TaskForgeAPI.mapper;

import com.flawden.TaskForgeAPI.dto.user.*;
import com.flawden.TaskForgeAPI.model.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "role", source = "role", qualifiedByName = "roleToString")
    User mapUserEntityToUser(UserEntity userEntity);

    @Mapping(target = "password", ignore = true)
    @Mapping(target = "taskEntities", ignore = true)
    @Mapping(target = "role", source = "role", qualifiedByName = "stringToRole")
    UserEntity mapUserToUserEntity(User user);

    UpdateUser mapUserEntityToUpdateUser(UserEntity userEntity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "taskEntities", ignore = true)
    UserEntity mapUpdateUserToUserEntity(UpdateUser updateUser);

    @Mapping(target = "email", source = "username")
    @Mapping(target = "role", expression = "java(getRoleFromString(register.getRole()))")
    UserEntity mapRegisterToUserEntity(Register register);

    default Role getRoleFromString(String role) {
        try {
            return role != null ? Role.valueOf(role.toUpperCase()) : null;
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    @Mapping(target = "username", source = "email")
    @Mapping(target = "role", expression = "java(userEntity.getRole() != null ? userEntity.getRole().name() : null)")
    Register mapUserEntityToRegister(UserEntity userEntity);

    @Mapping(target = "username", source = "email")
    Login mapUserEntityToLogin(UserEntity userEntity);

    @Mapping(target = "email", source = "username")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "firstName", ignore = true)
    @Mapping(target = "lastName", ignore = true)
    @Mapping(target = "role", ignore = true)
    UserEntity mapLoginToUserEntity(Login login);

    @Named("roleToString")
    static String roleToString(Role role) {
        return role != null ? role.name() : null;
    }

    @Named("stringToRole")
    static Role stringToRole(String role) {
        return role != null ? Role.valueOf(role.toUpperCase()) : null;
    }
}
