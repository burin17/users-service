package com.gmail.burinigor7.usersservice.service;

import com.gmail.burinigor7.usersservice.dao.RoleRepository;
import com.gmail.burinigor7.usersservice.domain.Role;
import com.gmail.burinigor7.usersservice.exception.RoleNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;

    public Role role(Long id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new RoleNotFoundException(id));
    }

    public Iterable<Role> all() {
        return roleRepository.findAll();
    }

    public Role roleByTitle(String title) {
        return roleRepository.findByTitle(title)
                .orElseThrow(() -> new RoleNotFoundException(title));
    }

    public Role newRole(Role role) {
        return roleRepository.save(role);
    }

    public Role replaceRole(Role newRole, Long id) {
        return roleRepository.findById(id)
                .map(fetched -> {
                    fetched.setTitle(newRole.getTitle());
                    return roleRepository.save(fetched);
                })
                .orElseThrow(() -> new RoleNotFoundException(id));
    }

    public void deleteRole(Long id) {
        roleRepository.deleteById(id);
    }
}
