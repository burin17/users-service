package com.gmail.burinigor7.userscrudservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthenticationRequestDto {
    private String login;
    private String password;
}
