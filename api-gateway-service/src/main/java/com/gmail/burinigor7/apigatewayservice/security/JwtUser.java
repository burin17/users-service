package com.gmail.burinigor7.apigatewayservice.security;

import com.gmail.burinigor7.apigatewayservice.dto.UserAuthDataDto;
import com.gmail.burinigor7.apigatewayservice.dto.Role;
import com.gmail.burinigor7.apigatewayservice.dto.Status;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.Collections;

public class JwtUser implements UserDetails {
    private static final String ROLE_PREFIX = "ROLE_";

    private final UserAuthDataDto user;

    public JwtUser(UserAuthDataDto user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(mapRoleToGrantedAuthority(user.getRole()));
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getLogin();
    }

    @Override
    public boolean isAccountNonExpired() {
        return user.getStatus() == Status.ACTIVE;
    }

    @Override
    public boolean isAccountNonLocked() {
        return user.getStatus() == Status.ACTIVE;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return user.getStatus() == Status.ACTIVE;
    }

    @Override
    public boolean isEnabled() {
        return user.getStatus() == Status.ACTIVE;
    }

    public UserAuthDataDto getUser() {
        return user;
    }

    private GrantedAuthority mapRoleToGrantedAuthority(Role role) {
        return new SimpleGrantedAuthority(ROLE_PREFIX + role.getTitle());
    }
}
