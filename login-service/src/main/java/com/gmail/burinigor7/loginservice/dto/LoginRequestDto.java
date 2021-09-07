package com.gmail.burinigor7.loginservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginRequestDto {
    private String login;
    private String password;
}
