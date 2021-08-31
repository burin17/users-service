package com.gmail.burinigor7.usersservice.dto;

import com.gmail.burinigor7.usersservice.domain.Role;
import com.gmail.burinigor7.usersservice.domain.Status;
import com.gmail.burinigor7.usersservice.domain.User;
import lombok.Data;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class RegistrationDto {
    @NotBlank(message = "'login' must not be blank")
    @Size(min = 4, message = "'login' must be longer then 3 characters")
    private String login;

    @NotBlank(message = "'password' must not be blank")
    @Size(min = 4, message = "'password' must be longer then 3 characters")
    private String password;

    @Email(message = "incorrect 'email'")
    private String email;

    @Pattern(regexp = "^\\+7\\d{10}$", message = "incorrect 'phoneNumber'")
    @NotNull
    private String phoneNumber;

    @NotBlank(message = "'firstName' must not be blank")
    private String firstName;

    @NotBlank(message = "'lastName' must not be blank")
    private String lastName;

    private String patronymic;

    public User toOrdinaryUser() {
        User user = new User();
        user.setRole(new Role(2L, null)); // User role id = 2 (preloaded)
        user.setStatus(Status.ACTIVE);
        user.setPassword(password);
        user.setLogin(login);
        user.setPassword(password);
        user.setEmail(email);
        user.setPhoneNumber(phoneNumber);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPatronymic(patronymic);
        return user;
    }
}
