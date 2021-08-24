package com.gmail.burinigor7.usersservice.util;

import com.gmail.burinigor7.usersservice.dao.RoleRepository;
import com.gmail.burinigor7.usersservice.domain.Role;
import com.gmail.burinigor7.usersservice.domain.User;
import com.gmail.burinigor7.usersservice.exception.UserRoleIdNotSpecifiedException;
import com.gmail.burinigor7.usersservice.exception.UserRoleNotPresentedException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class UserRoleValidatorTests {
    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private UserRoleValidator userRoleValidator;

    @Test
    public void validate_whenUserWithCorrectRolePassed_thenNoExceptionThrown() {
        User passed = new User();
        passed.setRole(new Role(1L, "Role1"));
        when(roleRepository.existsById(passed.getRole().getId())).thenReturn(true);
        userRoleValidator.validate(passed);
    }

    @Test
    public void validate_whenUserWithoutRoleIdPassed_thenExceptionThrown() {
        User passed = new User();
        passed.setRole(new Role(null, "Role1"));
        assertThrows(UserRoleIdNotSpecifiedException.class,
                () -> userRoleValidator.validate(passed));
    }

    @Test
    public void validate_whenUserWithNotExistentRolePassed_thenExceptionThrown() {
        User passed = new User();
        passed.setRole(new Role(1L, "Role1"));
        when(roleRepository.existsById(passed.getRole().getId())).thenReturn(false);
        assertThrows(UserRoleNotPresentedException.class,
                () -> userRoleValidator.validate(passed));
    }
}
