package com.gmail.burinigor7.usersservice.controller.advice;

import com.gmail.burinigor7.usersservice.util.JacksonViews;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.AbstractMappingJacksonResponseBodyAdvice;
import java.util.Collection;
import java.util.Optional;

@RestControllerAdvice
class SecurityJsonViewControllerAdvice extends AbstractMappingJacksonResponseBodyAdvice {
    private final static String ADMIN_ROLE_TITLE = "ROLE_ADMIN";

    @Override
    protected void beforeBodyWriteInternal(
            MappingJacksonValue bodyContainer,
            MediaType contentType,
            MethodParameter returnType,
            ServerHttpRequest request,
            ServerHttpResponse response) {
        if (SecurityContextHolder.getContext().getAuthentication() != null
                && SecurityContextHolder.getContext()
                .getAuthentication().getAuthorities() != null) {
            Collection<? extends GrantedAuthority> authorities
                    = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
            Optional<? extends Class<?>> jacksonViewClass = authorities.stream()
                    .map(GrantedAuthority::getAuthority)
                    .findAny()
                    .map(authority -> ADMIN_ROLE_TITLE.equals(authority)
                            ? JacksonViews.Admin.class : JacksonViews.User.class);
            jacksonViewClass.ifPresent(bodyContainer::setSerializationView);
        }
    }
}
