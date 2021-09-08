package com.gmail.burinigor7.admindeletionservice.controller;

import com.gmail.burinigor7.admindeletionservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/is-allowed/{id}")
    public boolean isDeletionAllowed(@PathVariable Long id) {
        System.out.println("java instance");
        return userService.isDeletionAllowed(id);
    }
}
