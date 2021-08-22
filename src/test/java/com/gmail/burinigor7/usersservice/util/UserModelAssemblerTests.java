package com.gmail.burinigor7.usersservice.util;

import com.gmail.burinigor7.usersservice.domain.Role;
import com.gmail.burinigor7.usersservice.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserModelAssemblerTests {
    public final UserModelAssembler userModelAssembler = new UserModelAssembler();

    @Test
    public void toModel_ordinaryRoleAsArgument_correctEntityModel() {
        final User argument = new User(1L, "Ivan", "Ivanov", "Petrovich",
                "89871111111", new Role(1L, "User"),
                "test@email.com", "ivanov1");
        EntityModel<User> userModel = userModelAssembler.toModel(argument);
        assertEquals(userModel.getContent(), argument);
        userModel.getRequiredLink(IanaLinkRelations.SELF);
        userModel.getRequiredLink("users");
    }
}
