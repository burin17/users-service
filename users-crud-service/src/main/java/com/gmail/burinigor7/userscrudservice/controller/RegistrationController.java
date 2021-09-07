package com.gmail.burinigor7.userscrudservice.controller;

import com.gmail.burinigor7.userscrudservice.controller.admin.UserController;
import com.gmail.burinigor7.userscrudservice.domain.User;
import com.gmail.burinigor7.userscrudservice.dto.RegistrationDto;
import com.gmail.burinigor7.userscrudservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${url.login}")
    private String loginHref;

    @PostMapping
    public ResponseEntity<?> register(@RequestBody @Valid RegistrationDto dto) {
        User createdUser = userService.newUser(dto.toOrdinaryUser());
        String loginRel = "login";
        EntityModel<User> userModel =
                EntityModel.of(createdUser,
                        Link.of(loginHref, loginRel),
                        linkTo(methodOn(UserController.class).user(createdUser.getId()))
                                .withSelfRel());
        return ResponseEntity.status(HttpStatus.CREATED).body(userModel);
    }
}
