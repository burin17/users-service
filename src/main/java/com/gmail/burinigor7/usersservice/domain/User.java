package com.gmail.burinigor7.usersservice.domain;

import lombok.Data;

@Data
public class User {
    private String firstName;
    private String lastName;
    private String patronymic;
    private String phoneNumber;
    private Role role;
    private String email;
    private String login;
}
