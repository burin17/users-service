package com.gmail.burinigor7.usersservice.util;

import com.gmail.burinigor7.usersservice.dao.RoleRepository;
import com.gmail.burinigor7.usersservice.domain.Role;
import com.gmail.burinigor7.usersservice.exception.RoleNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class RoleByTitleConverterTests {
    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleByTitleConverter roleByTitleConverter;

    @Test
    public void convert_existentRole_returnFetchedRole() {
        final String title = "Role1"; // existent role
        final Role fetched = new Role(1L, title);
        when(roleRepository.findByTitle(title)).thenReturn(Optional.of(fetched));
        Role converted = roleByTitleConverter.convert(title);
        assertEquals(fetched, converted);
    }

    @Test
    public void convert_notExistentRole_exceptionThrown() {
        final String title = "Role2"; // not existent role
        when(roleRepository.findByTitle(title)).thenReturn(Optional.empty());
        assertThrows(RoleNotFoundException.class, () -> roleByTitleConverter.convert(title));
    }
}
