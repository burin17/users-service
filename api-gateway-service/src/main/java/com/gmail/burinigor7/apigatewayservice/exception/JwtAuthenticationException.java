package com.gmail.burinigor7.apigatewayservice.exception;

import org.springframework.security.core.AuthenticationException;

public class JwtAuthenticationException
        extends AuthenticationException {
    public JwtAuthenticationException(String msg) {
        super(msg);
    }
}
