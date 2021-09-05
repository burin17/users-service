package com.gmail.burinigor7.userscrudservice.dto;

import com.gmail.burinigor7.userscrudservice.domain.User;
import lombok.Data;

@Data
public class UserDto {
    private String firstName;
    private String lastName;
    private String patronymic;
    private String phoneNumber;
    private String email;
    private String login;

    public UserDto(User user) {
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.patronymic = user.getPatronymic();
        this.phoneNumber = user.getPhoneNumber();
        this.email = user.getEmail();
        this.login = user.getLogin();
    }
}
