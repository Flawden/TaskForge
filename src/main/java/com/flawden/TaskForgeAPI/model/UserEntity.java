package com.flawden.TaskForgeAPI.model;

import com.flawden.TaskForgeAPI.dto.user.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString(exclude = "taskEntities")
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "user_entity")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Email не может быть пустым")
    @Email(message = "Некорректный формат email")
    private String email;

    @NotNull(message = "Пароль не может быть пустым")
    @Size(min = 8, message = "Пароль должен содержать хотя бы 8 символов")
    private String password;

    @NotNull(message = "Имя не может быть пустым")
    private String firstName;

    @NotNull(message = "Фамилия не может быть пустой")
    private String lastName;

    @NotNull(message = "Роль не может быть пустой")
    private Role role;

    @ManyToMany
    @JoinTable(
            name = "task_user",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "task_id")
    )
    private List<TaskEntity> taskEntities;

}
