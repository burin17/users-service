package com.gmail.burinigor7.usersservice.service;

import com.gmail.burinigor7.usersservice.dao.UserRepository;
import com.gmail.burinigor7.usersservice.domain.Role;
import com.gmail.burinigor7.usersservice.domain.User;
import com.gmail.burinigor7.usersservice.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

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
        return userRepository.save(user);
    }

    public User replaceUser(User newUser, Long id) {
        return userRepository.findById(id)
                .map(fetched -> userRepository.save(replaceUser(fetched, newUser)))
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
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
