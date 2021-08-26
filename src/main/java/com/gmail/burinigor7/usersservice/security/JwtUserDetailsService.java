package com.gmail.burinigor7.usersservice.security;

import com.gmail.burinigor7.usersservice.dao.UserRepository;
import com.gmail.burinigor7.usersservice.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByLogin(login);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User with login = " + login + " not found");
        }
        return new JwtUser(user.get());
    }
}
