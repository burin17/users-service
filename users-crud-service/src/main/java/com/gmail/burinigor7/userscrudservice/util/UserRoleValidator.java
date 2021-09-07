package com.gmail.burinigor7.userscrudservice.util;

import com.gmail.burinigor7.userscrudservice.dao.RoleRepository;
import com.gmail.burinigor7.userscrudservice.domain.User;
import com.gmail.burinigor7.userscrudservice.exception.UserRoleIdNotSpecifiedException;
import com.gmail.burinigor7.userscrudservice.exception.UserRoleNotPresentedException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserRoleValidator {
    public final RoleRepository roleRepository;

    public void validate(User user) {
        if(user.getRole().getId() == null) {
            throw new UserRoleIdNotSpecifiedException(
                    "Incorrect request body. Id of user's role must be specified.");
        }
        if(!roleRepository.existsById(user.getRole().getId())) {
            throw new UserRoleNotPresentedException(user.getRole().getId());
        }
    }
}
