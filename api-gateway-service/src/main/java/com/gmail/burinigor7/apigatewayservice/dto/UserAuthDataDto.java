package com.gmail.burinigor7.apigatewayservice.dto;

import lombok.Data;

@Data
public class UserAuthDataDto {
    private Long id;
    private String login;
    private String password;
    private Role role;
    private Status status;
}
