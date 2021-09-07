package com.gmail.burinigor7.userscrudservice.exception;

public class NoGrantsToDeleteAdminException extends RuntimeException {
    public NoGrantsToDeleteAdminException(String message) {
        super(message);
    }
}
