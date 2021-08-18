package com.gmail.burinigor7.usersservice.domain;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "usr")
public class User {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    private String patronymic;

    @Column(name = "phone_number")
    private String phoneNumber;

    @ManyToOne
    @JoinColumn(name = "user_role")
    private Role role;
    private String email;
    private String login;
}
