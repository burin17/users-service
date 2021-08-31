package com.gmail.burinigor7.usersservice.controller;

import com.gmail.burinigor7.usersservice.domain.Role;
import com.gmail.burinigor7.usersservice.exception.RoleNotFoundException;
import com.gmail.burinigor7.usersservice.service.RoleService;
import com.gmail.burinigor7.usersservice.util.RoleModelAssembler;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class RoleController {
    private final RoleService roleService;
    private final RoleModelAssembler assembler;

    @GetMapping(value = "/{id}", produces = "application/json")
    public EntityModel<Role> role(@PathVariable Long id) {
        Role role = roleService.role(id);
        return assembler.toModel(role);
    }

    @GetMapping(produces = "application/json")
    public CollectionModel<EntityModel<Role>> all() {
        List<EntityModel<Role>> roleModels = StreamSupport
                .stream(roleService.all().spliterator(), false)
                .map(assembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(roleModels,
                linkTo(methodOn(RoleController.class).all()).withSelfRel());
    }

    @GetMapping(params = "title", produces = "application/json")
    public EntityModel<Role> roleByTitle(@RequestParam String title) {
        Role role = roleService.roleByTitle(title);
        return assembler.toModel(role);
    }

    @PostMapping(produces = "application/json")
    @ApiResponses({
            @ApiResponse(code = 409, message="Conflict with existent role's 'title' value"),
            @ApiResponse(code = 422, message="Incorrect 'title' of request body")
    })
    public ResponseEntity<EntityModel<Role>> newRole(@RequestBody @Valid Role role) {
        EntityModel<Role> roleModel = assembler.toModel(roleService.newRole(role));
        return ResponseEntity
                .created(roleModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(roleModel);
    }

    @PutMapping(value = "/{id}", produces = "application/json")
    @ApiResponses({
            @ApiResponse(code = 409, message="Conflict with existent role's 'title' value"),
            @ApiResponse(code = 422, message="Incorrect 'title' of request body")
    })
    public ResponseEntity<EntityModel<Role>> replaceRole(@RequestBody @Valid Role newRole,
                                                         @PathVariable Long id) {
        EntityModel<Role> roleModel = assembler
                .toModel(roleService.replaceRole(newRole, id));
        return ResponseEntity
                .created(roleModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(roleModel);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRole(@PathVariable Long id) {
        roleService.deleteRole(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(RoleNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String userNotFoundExceptionHandler(RoleNotFoundException exception) {
        return exception.getMessage();
    }
}
