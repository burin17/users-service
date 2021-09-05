package com.gmail.burinigor7.loginservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UserTokenDataDto {
    private String login;

    @JsonProperty("password")
    private String encryptedPassword;

    private Role role;
}
