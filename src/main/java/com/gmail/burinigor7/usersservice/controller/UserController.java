package com.gmail.burinigor7.usersservice.controller;

import com.gmail.burinigor7.usersservice.domain.Role;
import com.gmail.burinigor7.usersservice.domain.User;
import com.gmail.burinigor7.usersservice.exception.UserNotFoundException;
import com.gmail.burinigor7.usersservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/{id}")
    public User user(@PathVariable Long id) {
        return userService.user(id);
    }

    @GetMapping
    public Iterable<User> all() {
        return userService.all();
    }

    @GetMapping(params = "email")
    public User userByEmail(@RequestParam String email) {
        return userService.userByEmail(email);
    }

    @GetMapping(params = "login")
    public User userByLogin(@RequestParam String login) {
        return userService.userByLogin(login);
    }

    @GetMapping("/filter-by-name")
    public List<User> usersByName(
            @RequestParam(value = "first-name", required = false) String firstName,
            @RequestParam(value = "last-name", required = false) String lastName,
            @RequestParam(value = "patronymic", required = false) String patronymic) {
        return userService.usersByName(firstName, lastName, patronymic);
    }

    @GetMapping(params = "role")
    public Iterable<User> usersByRole(@RequestParam Role role) {
        return userService.usersByRole(role);
    }

    @GetMapping(params = "phone-number")
    public User usersByPhoneNumber(@RequestParam("phone-number") String phoneNumber) {
        return userService.userByPhoneNumber(phoneNumber);
    }

    @PostMapping
    public User newUser(@RequestBody User user) {
        return userService.newUser(user);
    }

    @PutMapping("/{id}")
    public User replaceUser(@RequestBody User newUser, @PathVariable Long id) {
        return userService.replaceUser(newUser, id);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String userNotFoundExceptionHandler(UserNotFoundException exception) {
        return exception.getMessage();
    }
}
