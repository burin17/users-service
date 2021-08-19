package com.gmail.burinigor7.usersservice.controller;

import com.gmail.burinigor7.usersservice.dao.UserRepository;
import com.gmail.burinigor7.usersservice.domain.Role;
import com.gmail.burinigor7.usersservice.domain.User;
import com.gmail.burinigor7.usersservice.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
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
public class UserController {
    private final UserRepository userRepository;

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/{id}")
    public User user(@PathVariable Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    @GetMapping
    public Iterable<User> all() {
        return userRepository.findAll();
    }

    @GetMapping(params = "email")
    public User userByEmail(@RequestParam String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User with email '" +
                        email + "' not found."));
    }

    @GetMapping(params = "login")
    public User userByLogin(@RequestParam String login) {
        return userRepository.findByLogin(login)
                .orElseThrow(() -> new UserNotFoundException("User with login '" +
                        login + "' not found."));
    }

    @GetMapping("/filter-by-name")
    public List<User> usersByName(
            @RequestParam(value = "first-name", required = false) String firstName,
            @RequestParam(value = "last-name", required = false) String lastName,
            @RequestParam(value = "patronymic", required = false) String patronymic) {
        User userExample = new User();
        userExample.setFirstName(firstName);
        userExample.setLastName(lastName);
        userExample.setPatronymic(patronymic);
        return userRepository.findAll(Example.of(userExample));
    }

    @GetMapping(params = "role")
    public Iterable<User> usersByRole(@RequestParam Role role) {
        return userRepository.findByRole(role);
    }

    @GetMapping(params = "phone-number")
    public User usersByPhoneNumber(@RequestParam("phone-number") String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new UserNotFoundException("User with phone number '" +
                        phoneNumber + "' not found."));
    }

    @PostMapping
    public User newUser(@RequestBody User user) {
        return userRepository.save(user);
    }

    @PutMapping("/{id}")
    public User replaceUser(@RequestBody User newUser, @PathVariable Long id) {
        return userRepository.findById(id)
                .map(fetched -> userRepository.save(replaceUser(fetched, newUser)))
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String userNotFoundExceptionHandler(UserNotFoundException exception) {
        return exception.getMessage();
    }

    private User replaceUser(User fetched, User newUser) {
        fetched.setFirstName(newUser.getFirstName());
        fetched.setLastName(newUser.getLastName());
        fetched.setPatronymic(newUser.getPatronymic());
        fetched.setPhoneNumber(newUser.getPhoneNumber());
        fetched.setRole(newUser.getRole());
        fetched.setEmail(newUser.getEmail());
        fetched.setLogin(newUser.getLogin());
        return fetched;
    }
}
