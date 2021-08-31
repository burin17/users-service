package com.gmail.burinigor7.usersservice.service;

import com.gmail.burinigor7.usersservice.dao.UserRepository;
import com.gmail.burinigor7.usersservice.domain.Role;
import com.gmail.burinigor7.usersservice.domain.User;
import com.gmail.burinigor7.usersservice.exception.UserNotFoundException;
import com.gmail.burinigor7.usersservice.util.UserRoleValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserRoleValidator userRoleValidator;
    private final PasswordEncoder passwordEncoder;

    public User user(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    public Iterable<User> all() {
        return userRepository.findAll();
    }

    public User userByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User with email '" +
                        email + "' not found."));
    }

    public User userByLogin(String login) {
        return userRepository.findByLogin(login)
                .orElseThrow(() -> new UserNotFoundException("User with login '" +
                        login + "' not found."));
    }

    public List<User> usersByName(String firstName, String lastName, String patronymic) {
        User userExample = new User();
        userExample.setFirstName(firstName);
        userExample.setLastName(lastName);
        userExample.setPatronymic(patronymic);
        return userRepository.findAll(Example.of(userExample));
    }

    public Iterable<User> usersByRole(Role role) {
        return userRepository.findByRole(role);
    }

    public User userByPhoneNumber(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new UserNotFoundException("User with phone number '" +
                        phoneNumber + "' not found."));
    }

    public User newUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRoleValidator.validate(user);
        return userRepository.save(user);
    }

    public User replaceUser(User newUser, Long id) {
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        userRoleValidator.validate(newUser);
        return userRepository.findById(id)
                .map(fetched -> userRepository.save(replaceUser(fetched, newUser)))
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    public void deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
        } else {
            throw new UserNotFoundException(id);
        }
    }

    private User replaceUser(User fetched, User newUser) {
        fetched.setFirstName(newUser.getFirstName());
        fetched.setLastName(newUser.getLastName());
        fetched.setPatronymic(newUser.getPatronymic());
        fetched.setPhoneNumber(newUser.getPhoneNumber());
        fetched.setRole(newUser.getRole());
        fetched.setEmail(newUser.getEmail());
        fetched.setLogin(newUser.getLogin());
        fetched.setPassword(newUser.getPassword());
        fetched.setStatus(newUser.getStatus());
        return fetched;
    }
}
