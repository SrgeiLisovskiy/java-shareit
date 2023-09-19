package ru.practicum.shareit.user;

import lombok.*;

import javax.persistence.*;


@Entity
@Table(name = "users")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;
    @Column(name = "name", nullable = false, length = 50)
    private String name;


}
