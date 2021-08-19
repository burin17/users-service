package com.gmail.burinigor7.usersservice.controller;

import com.gmail.burinigor7.usersservice.dao.RoleRepository;
import com.gmail.burinigor7.usersservice.domain.Role;
import com.gmail.burinigor7.usersservice.exception.RoleNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/roles")
public class RoleController {
    private final RoleRepository roleRepository;

    @Autowired
    public RoleController(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @GetMapping("/{id}")
    public Role role(@PathVariable Long id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new RoleNotFoundException(id));
    }

    @GetMapping
    public Iterable<Role> all() {
        return roleRepository.findAll();
    }

    @GetMapping(params = "title")
    public Role roleByTitle(@RequestParam String title) {
        return roleRepository.findByTitle(title)
                .orElseThrow(() -> new RoleNotFoundException(title));
    }

    @PostMapping
    public Role newRole(@RequestBody Role role) {
        return roleRepository.save(role);
    }

    @PutMapping("/{id}")
    public Role replaceRole(@RequestBody Role newRole, @PathVariable Long id) {
        return roleRepository.findById(id)
                .map(fetched -> {
                    fetched.setTitle(newRole.getTitle());
                    return roleRepository.save(fetched);
                })
                .orElseGet(() -> {
                    newRole.setId(id);
                    return roleRepository.save(newRole);
                });
    }

    @DeleteMapping("/{id}")
    public void deleteRole(@PathVariable Long id) {
        roleRepository.deleteById(id);
    }
}
