package com.gmail.burinigor7.usersservice.util;

import com.gmail.burinigor7.usersservice.controller.admin.UserController;
import com.gmail.burinigor7.usersservice.domain.User;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UserModelAssembler
        implements RepresentationModelAssembler<User, EntityModel<User>> {
    @Override
    public EntityModel<User> toModel(User user) {
        String aggregateRootRel = "users";
        return EntityModel.of(user,
                linkTo(methodOn(UserController.class).user(user.getId())).withSelfRel(),
                linkTo(methodOn(UserController.class).all()).withRel(aggregateRootRel));
    }
}
