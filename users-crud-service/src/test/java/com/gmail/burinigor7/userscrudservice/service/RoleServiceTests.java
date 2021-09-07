package com.gmail.burinigor7.userscrudservice.service;

import com.gmail.burinigor7.userscrudservice.dao.RoleRepository;
import com.gmail.burinigor7.userscrudservice.domain.Role;
import com.gmail.burinigor7.userscrudservice.exception.RoleNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class RoleServiceTests {
    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleService roleService;

    @Test
    public void role_existentRole_returnFetchedRole() {
        final long id = 1L; // existent role
        final Role fetched = new Role(id, "User");
        when(roleRepository.findById(id)).thenReturn(Optional.of(fetched));
        Role returned = roleService.role(id);
        assertEquals(fetched, returned);
    }

    @Test
    public void role_notExistentRole_exceptionThrown() {
        final long id = 2L; // not existent role
        when(roleRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(RoleNotFoundException.class, () -> roleService.role(id));
    }

    @Test
    public void all_anyPersistedRoles_returnFetchedRoles() {
        Iterable<Role> fetched = List.of(
                new Role(1L, "User"),
                new Role(2L, "Admin")
        );
        when(roleRepository.findAll()).thenReturn(fetched);
        Iterable<Role> returned = roleService.all();
        assertEquals(fetched, returned);
    }

    @Test
    public void roleByTitle_existentRole_returnFetchedRole() {
        final String title = "Role1"; // existent role
        final Role fetched = new Role(1L, title);
        when(roleRepository.findByTitle(title)).thenReturn(Optional.of(fetched));
        Role returned = roleService.roleByTitle(title);
        assertEquals(fetched, returned);
    }

    @Test
    public void roleByTitle_notExistentRole_exceptionThrown() {
        final String title = "Role1"; // not existent role
        when(roleRepository.findByTitle(title)).thenReturn(Optional.empty());
        assertThrows(RoleNotFoundException.class, () -> roleService.roleByTitle(title));
    }

    @Test
    public void newRole_persistRole_returnRoleWithTheSameTitleAndWithId() {
        final String newRoleTitle = "Role1";
        final Role newRole = new Role(null, newRoleTitle);
        when(roleRepository.save(newRole)).thenReturn(new Role(1L, "Role1"));
        Role returned = roleService.newRole(newRole);
        assertEquals(newRoleTitle, returned.getTitle());
        assertNotNull(returned.getId());
    }

    @Test
    public void replaceRole_existentRoleReplacement_returnRoleWithNewData() {
        final long roleId = 1L;
        final String newRoleTitle = "NewRoleTitle";
        final Role persistent = new Role(roleId, "OldRoleTitle");
        when(roleRepository.findById(roleId)).thenReturn(Optional.of(persistent));
        when(roleRepository.save(persistent)).thenReturn(persistent);
        Role returned = roleService.replaceRole(new Role(null, newRoleTitle), roleId);
        assertEquals(newRoleTitle, returned.getTitle());
    }

    @Test
    public void replaceRole_notExistentRoleReplacement_exceptionThrown() {
        final long roleId = 1L; // not existent role id
        final String newRoleTitle = "NewRoleTitle";
        when(roleRepository.findById(roleId)).thenReturn(Optional.empty());
        assertThrows(RoleNotFoundException.class, () -> roleService
                .replaceRole(new Role(null, newRoleTitle), roleId));
    }

    @Test
    public void deleteRole_whenRoleExists_thenNoExceptionThrown() {
        long id = 1L;
        when(roleRepository.existsById(id)).thenReturn(true);
        roleService.deleteRole(id);
    }

    @Test
    public void deleteRole_whenRoleNotExists_thenExceptionThrown() {
        long id = 1L;
        when(roleRepository.existsById(id)).thenReturn(false);
        assertThrows(RoleNotFoundException.class,
                () -> roleService.deleteRole(id));
    }
}
