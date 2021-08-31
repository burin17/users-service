package com.gmail.burinigor7.userscrudservice.exception;

public class UserRoleNotPresentedException extends RuntimeException {
    public UserRoleNotPresentedException(Long id) {
        super("Role with id = " + id + " not presented.");
    }
}
