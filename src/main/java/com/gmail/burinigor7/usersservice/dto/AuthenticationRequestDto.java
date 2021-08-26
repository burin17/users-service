package com.gmail.burinigor7.usersservice.dto;

import lombok.Data;

@Data
public class AuthenticationRequestDto {
    private String login;
    private String password;
}
