package com.gmail.burinigor7.apigatewayservice.security;

import com.gmail.burinigor7.apigatewayservice.dto.UserAuthDataDto;
import com.gmail.burinigor7.apigatewayservice.internalcalls.UserFeignInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {
    private final UserFeignInterface userClient;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Optional<UserAuthDataDto> user = userClient.fetchUserByLogin(login);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User with login = " + login + " not found");
        }
        return new JwtUser(user.get());
    }
}
