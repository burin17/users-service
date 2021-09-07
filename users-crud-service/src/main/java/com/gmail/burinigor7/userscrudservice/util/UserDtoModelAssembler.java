package com.gmail.burinigor7.userscrudservice.util;

import com.gmail.burinigor7.userscrudservice.controller.user.UserController;
import com.gmail.burinigor7.userscrudservice.dto.UserDto;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UserDtoModelAssembler
        implements RepresentationModelAssembler<UserDto, EntityModel<UserDto>> {
    @Override
    public EntityModel<UserDto> toModel(UserDto user) {
        String aggregateRootRel = "usersDto";
        return EntityModel.of(user,
                linkTo(methodOn(UserController.class).userByLogin(user.getLogin())).withSelfRel(),
                linkTo(methodOn(UserController.class).all()).withRel(aggregateRootRel));
    }
}
