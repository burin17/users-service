package com.gmail.burinigor7.userscrudservice.exception;

public class RoleNotFoundException extends RuntimeException {
    public RoleNotFoundException(Long id) {
        super("Role with id = " + id +" not found.");
    }
    public RoleNotFoundException(String title) {
        super("Role '" + title + "' not found");
    }
}
