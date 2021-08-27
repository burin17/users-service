package com.gmail.burinigor7.usersservice.dto;

import com.gmail.burinigor7.usersservice.domain.Status;
import com.gmail.burinigor7.usersservice.domain.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RegistrationDtoTests {

    @Test
    public void toOrdinaryUser_whenInvoked_returnsUserWithUserRoleAndActiveStatus() {
        RegistrationDto dto = new RegistrationDto();
        dto.setLogin("login");
        dto.setEmail("email@email.com");
        dto.setPassword("pass");
        dto.setFirstName("firstName");
        dto.setLastName("lastName");
        dto.setPhoneNumber("+70000000000");
        User ordinaryUser = dto.toOrdinaryUser();
        assertEquals(Status.ACTIVE, ordinaryUser.getStatus());
        assertEquals(2L, ordinaryUser.getRole().getId());
    }
}
