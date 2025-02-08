package com.flawden.TaskForgeAPI.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString(exclude = "task")
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "comment_entity")
public class CommentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Текст комментария не может быть пустым")
    private String text;

    @ManyToOne
    @NotNull(message = "Автор комментария не может быть пустым")
    private UserEntity author;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @NotNull(message = "Задача не может быть пустой")
    private TaskEntity task;
}
