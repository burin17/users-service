package com.gmail.burinigor7.usersservice.controller;

import com.gmail.burinigor7.usersservice.domain.Role;
import com.gmail.burinigor7.usersservice.domain.User;
import com.gmail.burinigor7.usersservice.exception.UserNotFoundException;
import com.gmail.burinigor7.usersservice.service.UserService;
import com.gmail.burinigor7.usersservice.util.UserModelAssembler;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserModelAssembler assembler;

    @GetMapping("/{id}")
    public EntityModel<User> user(@PathVariable Long id) {
        User user = userService.user(id);
        return assembler.toModel(user);
    }

    @GetMapping
    public CollectionModel<EntityModel<User>> all() {
        List<EntityModel<User>> userModels = StreamSupport
                .stream(userService.all().spliterator(), false)
                .map(assembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(userModels,
                linkTo(methodOn(UserController.class).all()).withSelfRel());
    }

    @GetMapping(params = "email")
    public EntityModel<User> userByEmail(@RequestParam String email) {
        User user = userService.userByEmail(email);
        return assembler.toModel(user);
    }

    @GetMapping(params = "login")
    public EntityModel<User> userByLogin(@RequestParam String login) {
        User user = userService.userByLogin(login);
        return assembler.toModel(user);
    }

    @GetMapping("/filter-by-name")
    public CollectionModel<EntityModel<User>> usersByName(
            @RequestParam(value = "first-name", required = false) String firstName,
            @RequestParam(value = "last-name", required = false) String lastName,
            @RequestParam(value = "patronymic", required = false) String patronymic) {
        String aggregateRootRel = "users";
        List<EntityModel<User>> userModels = userService
                .usersByName(firstName, lastName, patronymic)
                .stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(userModels,
                linkTo(methodOn(UserController.class)
                        .usersByName(firstName, lastName, patronymic))
                        .withSelfRel(),
                linkTo(methodOn(UserController.class).all()).withRel(aggregateRootRel));
    }

    @GetMapping(params = "role")
    public CollectionModel<EntityModel<User>> usersByRole(@RequestParam Role role) {
        String aggregateRootRel = "users";
        List<EntityModel<User>> userModels = StreamSupport
                .stream(userService.usersByRole(role).spliterator(), false)
                .map(assembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(userModels,
                linkTo(methodOn(UserController.class).usersByRole(role))
                        .withSelfRel(),
                linkTo(methodOn(UserController.class).all())
                        .withRel(aggregateRootRel));
    }

    @GetMapping(params = "phone-number")
    public EntityModel<User> usersByPhoneNumber(@RequestParam("phone-number") String phoneNumber) {
        User user = userService.userByPhoneNumber(phoneNumber);
        return assembler.toModel(user);
    }

    @PostMapping
    public ResponseEntity<EntityModel<User>> newUser(@RequestBody User user) {
        EntityModel<User> userModel = assembler.toModel(userService.newUser(user));
        return ResponseEntity.created(userModel.getRequiredLink(IanaLinkRelations.SELF)
                .toUri()).body(userModel);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<User>> replaceUser(@RequestBody User newUser, @PathVariable Long id) {
        EntityModel<User> userModel = assembler.toModel(userService.replaceUser(newUser, id));
        return ResponseEntity.created(userModel.getRequiredLink(IanaLinkRelations.SELF)
                .toUri()).body(userModel);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String userNotFoundExceptionHandler(UserNotFoundException exception) {
        return exception.getMessage();
    }
}
