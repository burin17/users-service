package com.gmail.burinigor7.usersservice.util;

import com.gmail.burinigor7.usersservice.controller.admin.RoleController;
import com.gmail.burinigor7.usersservice.domain.Role;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class RoleModelAssembler
        implements RepresentationModelAssembler<Role, EntityModel<Role>> {
    @Override
    public EntityModel<Role> toModel(Role role) {
        String aggregateRootRel = "roles";
        return EntityModel.of(role,
                linkTo(methodOn(RoleController.class).role(role.getId())).withSelfRel(),
                linkTo(methodOn(RoleController.class).all()).withRel(aggregateRootRel));
    }
}
