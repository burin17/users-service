package com.gmail.burinigor7.userscrudservice.util;

import com.gmail.burinigor7.userscrudservice.dao.RoleRepository;
import com.gmail.burinigor7.userscrudservice.domain.Role;
import com.gmail.burinigor7.userscrudservice.exception.RoleNotFoundException;
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
