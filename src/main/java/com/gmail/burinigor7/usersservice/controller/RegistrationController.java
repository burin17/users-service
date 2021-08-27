package com.gmail.burinigor7.usersservice.controller;

import com.gmail.burinigor7.usersservice.controller.admin.UserController;
import com.gmail.burinigor7.usersservice.domain.User;
import com.gmail.burinigor7.usersservice.dto.RegistrationDto;
import com.gmail.burinigor7.usersservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/registration")
@RequiredArgsConstructor
public class RegistrationController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<?> register(@RequestBody @Valid RegistrationDto dto) {
        User createdUser = userService.newUser(dto.toOrdinaryUser());
        createdUser.setPassword(null);
        EntityModel<User> userModel =
                EntityModel.of(createdUser,
                        Link.of("http://localhost:8080/auth/login", "login"),
                        linkTo(methodOn(UserController.class).user(createdUser.getId()))
                                .withSelfRel());
        return ResponseEntity.status(HttpStatus.CREATED).body(userModel);
    }
}
