package com.gmail.burinigor7.apigatewayservice.controller;

import com.gmail.burinigor7.apigatewayservice.internalcalls.UserFeignInterface;
import com.gmail.burinigor7.apigatewayservice.security.JwtUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
public class AdminDeletionController {
    private final UserFeignInterface userClient;

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id, Authentication authentication) {
        Long currentId = ((JwtUser) authentication.getPrincipal()).getUser()
                .getId();
        userClient.deleteUser(id, currentId);
    }
}
