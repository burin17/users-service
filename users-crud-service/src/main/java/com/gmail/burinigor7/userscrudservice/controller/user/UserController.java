package com.gmail.burinigor7.userscrudservice.controller.user;

import com.gmail.burinigor7.userscrudservice.domain.User;
import com.gmail.burinigor7.userscrudservice.dto.UserDto;
import com.gmail.burinigor7.userscrudservice.exception.AdminDeletionServiceNotAccessibleException;
import com.gmail.burinigor7.userscrudservice.exception.NoGrantsToDeleteAdminException;
import com.gmail.burinigor7.userscrudservice.exception.UserNotFoundException;
import com.gmail.burinigor7.userscrudservice.exception.UserRoleIdNotSpecifiedException;
import com.gmail.burinigor7.userscrudservice.exception.UserRoleNotPresentedException;
import com.gmail.burinigor7.userscrudservice.service.UserService;
import com.gmail.burinigor7.userscrudservice.util.UserDtoModelAssembler;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    private final UserDtoModelAssembler assembler;

    @GetMapping(value = "/{id}", produces = "application/json")
    public EntityModel<UserDto> user(@PathVariable Long id) {
        User user = userService.user(id);
        return assembler.toModel(new UserDto(user));
    }

    @GetMapping(produces = "application/json")
    public CollectionModel<EntityModel<UserDto>> all() {
        List<EntityModel<UserDto>> userDtoModels = StreamSupport
                .stream(userService.all().spliterator(), false)
                .map(user -> assembler.toModel(new UserDto(user)))
                .collect(Collectors.toList());
        return CollectionModel.of(userDtoModels,
                linkTo(methodOn(UserController.class).all()).withSelfRel());
    }

    @GetMapping(params = "email", produces = "application/json")
    public EntityModel<UserDto> userByEmail(@RequestParam String email) {
        User user = userService.userByEmail(email);
        return assembler.toModel(new UserDto(user));
    }

    @GetMapping(params = "login", produces = "application/json")
    public EntityModel<UserDto> userByLogin(@RequestParam String login) {
        User user = userService.userByLogin(login);
        return assembler.toModel(new UserDto(user));
    }

    @GetMapping(value = "/filter-by-name", produces = "application/json")
    public CollectionModel<EntityModel<UserDto>> usersByName(
            @RequestParam(value = "first-name", required = false) String firstName,
            @RequestParam(value = "last-name", required = false) String lastName,
            @RequestParam(value = "patronymic", required = false) String patronymic) {
        String aggregateRootRel = "usersDto";
        List<EntityModel<UserDto>> userModels = userService
                .usersByName(firstName, lastName, patronymic)
                .stream()
                .map(user -> assembler.toModel(new UserDto(user)))
                .collect(Collectors.toList());
        return CollectionModel.of(userModels,
                linkTo(methodOn(UserController.class)
                        .usersByName(firstName, lastName, patronymic))
                        .withSelfRel(),
                linkTo(methodOn(UserController.class).all()).withRel(aggregateRootRel));
    }

    @GetMapping(params = "phone-number", produces = "application/json")
    public EntityModel<UserDto> usersByPhoneNumber(
            @RequestParam("phone-number") String phoneNumber) {
        User user = userService.userByPhoneNumber(phoneNumber);
        return assembler.toModel(new UserDto(user));
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String userNotFoundExceptionHandler(UserNotFoundException exception) {
        return exception.getMessage();
    }

    @ExceptionHandler({
            UserRoleIdNotSpecifiedException.class,
            UserRoleNotPresentedException.class
    })
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public String userRoleExceptionsHandler(Exception exception) {
        return exception.getMessage();
    }

    @ExceptionHandler(NoGrantsToDeleteAdminException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String noGrantsToDeleteAdminExceptionHandler(NoGrantsToDeleteAdminException exception) {
        return exception.getMessage();
    }

    @ExceptionHandler(AdminDeletionServiceNotAccessibleException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE) // 503
    public String adminDeletionServiceNotAccessibleExceptionHandler(
            AdminDeletionServiceNotAccessibleException exception) {
        return exception.getMessage();
    }
}
