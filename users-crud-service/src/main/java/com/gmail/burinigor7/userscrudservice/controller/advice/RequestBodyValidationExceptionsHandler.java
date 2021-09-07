package com.gmail.burinigor7.userscrudservice.controller.advice;

import org.postgresql.util.PSQLException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestControllerAdvice
public class RequestBodyValidationExceptionsHandler {
    private static final Map<String, String> EXCEPTION_MESSAGE_TO_COLUMN =
            Map.of("Key (email)=", "email",
                   "Key (login)=", "login",
                   "Key (phone_number)=", "phone number",
                   "Key (title)=", "title");

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY) // 422
    public Map<String, String> methodArgumentNotValidExceptionHandler(
            MethodArgumentNotValidException exception) {
        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

    @ExceptionHandler(PSQLException.class)
    @ResponseStatus(HttpStatus.CONFLICT) // 409
    public String constraintsViolationHandler(PSQLException e) {
        String exceptionMessage = e.getMessage();
        Optional<String> res = EXCEPTION_MESSAGE_TO_COLUMN.entrySet().stream()
                .filter(entry -> exceptionMessage.contains(entry.getKey()))
                .map(Map.Entry::getValue)
                .findAny();
        return res.map(s -> "Resource with specified '" + s + "' already presented")
                .orElse(exceptionMessage);
    }
}
