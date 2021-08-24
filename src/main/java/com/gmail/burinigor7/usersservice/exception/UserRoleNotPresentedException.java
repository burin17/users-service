package com.gmail.burinigor7.usersservice.exception;

public class UserRoleNotPresentedException extends RuntimeException {
    public UserRoleNotPresentedException(Long id) {
        super("Role with id = " + id + " not presented.");
    }
}
