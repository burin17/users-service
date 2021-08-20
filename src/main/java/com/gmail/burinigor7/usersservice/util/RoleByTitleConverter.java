package com.gmail.burinigor7.usersservice.util;

import com.gmail.burinigor7.usersservice.dao.RoleRepository;
import com.gmail.burinigor7.usersservice.domain.Role;
import com.gmail.burinigor7.usersservice.exception.RoleNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class RoleByTitleConverter implements Converter<String, Role> {
    private final RoleRepository roleRepository;

    @Autowired
    public RoleByTitleConverter(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role convert(String title) {
        return roleRepository.findByTitle(title)
                .orElseThrow(() -> new RoleNotFoundException(title));
    }
}
