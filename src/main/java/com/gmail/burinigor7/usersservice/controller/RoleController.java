package com.gmail.burinigor7.usersservice.controller;

import com.gmail.burinigor7.usersservice.domain.Role;
import com.gmail.burinigor7.usersservice.exception.RoleNotFoundException;
import com.gmail.burinigor7.usersservice.service.RoleService;
import lombok.RequiredArgsConstructor;
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

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
public class RoleController {
    private final RoleService roleService;

    @GetMapping("/{id}")
    public Role role(@PathVariable Long id) {
        return roleService.role(id);
    }

    @GetMapping
    public Iterable<Role> all() {
        return roleService.all();
    }

    @GetMapping(params = "title")
    public Role roleByTitle(@RequestParam String title) {
        return roleService.roleByTitle(title);
    }

    @PostMapping
    public Role newRole(@RequestBody Role role) {
        return roleService.newRole(role);
    }

    @PutMapping("/{id}")
    public Role replaceRole(@RequestBody Role newRole, @PathVariable Long id) {
        return roleService.replaceRole(newRole, id);
    }

    @DeleteMapping("/{id}")
    public void deleteRole(@PathVariable Long id) {
        roleService.deleteRole(id);
    }

    @ExceptionHandler(RoleNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String userNotFoundExceptionHandler(RoleNotFoundException exception) {
        return exception.getMessage();
    }
}
