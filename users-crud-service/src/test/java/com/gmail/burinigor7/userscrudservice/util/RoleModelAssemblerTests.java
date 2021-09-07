package com.gmail.burinigor7.userscrudservice.util;

import com.gmail.burinigor7.userscrudservice.domain.Role;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RoleModelAssemblerTests {
    private final RoleModelAssembler roleModelAssembler = new RoleModelAssembler();

    @Test
    public void toModel_ordinaryRoleAsArgument_correctEntityModel() {
        Role argument = new Role(1L, "Role1");
        EntityModel<Role> roleModel = roleModelAssembler.toModel(argument);
        assertEquals(roleModel.getContent(), argument);
        roleModel.getRequiredLink(IanaLinkRelations.SELF);
        roleModel.getRequiredLink("roles");
    }
}
