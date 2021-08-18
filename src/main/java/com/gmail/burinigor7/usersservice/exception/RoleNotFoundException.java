package com.gmail.burinigor7.usersservice.exception;

public class RoleNotFoundException extends RuntimeException {
    public RoleNotFoundException(String title) {
        super("Role '" + title + "' not found");
    }
}
