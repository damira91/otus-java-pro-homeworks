package ru.otus.kudaiberdieva.homework14.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@ToString
@Builder
@AllArgsConstructor
@Table(name = "users")
public class User {
    @Id
    private Long id;

    private String name;

    private String email;

    private String phone;
}
