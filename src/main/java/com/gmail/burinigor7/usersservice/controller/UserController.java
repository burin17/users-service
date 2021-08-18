package com.gmail.burinigor7.usersservice.controller;

import com.gmail.burinigor7.usersservice.domain.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @GetMapping("/{id}")
    public User user(@PathVariable Long id) {
        return null;
    }

    @GetMapping
    public List<User> all() {
        return null;
    }

    @GetMapping(params = "email")
    public User userByEmail(@RequestParam String email) {
        return null;
    }

    @GetMapping(params = "login")
    public User userByLogin(@RequestParam String login) {
        return null;
    }

    @GetMapping("/filter-by-name")
    public List<User> qwe(@RequestParam(value = "first-name", required = false) String firstName,
                          @RequestParam(value = "last-name", required = false) String lastName,
                          @RequestParam(value = "patronymic", required = false) String patronymic) {
        return null;
    }


    @GetMapping(params = "role")
    public List<User> usersByRole(@RequestParam String role) {
        return null;
    }

    @GetMapping(params = "phone-number")
    public List<User> usersByPhoneNumber(@RequestParam String phoneNumber) {
        return null;
    }

    @PostMapping
    public User newUser(@RequestBody User user) {
        return null;
    }

    @PutMapping("/{id}")
    public User replaceUser(@RequestBody User user, @PathVariable Long id) {
        return null;
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {

    }
}
