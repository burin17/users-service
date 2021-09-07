package com.gmail.burinigor7.loginservice.service;

import com.gmail.burinigor7.loginservice.dto.LoginRequestDto;
import com.gmail.burinigor7.loginservice.dto.UserTokenDataDto;
import com.gmail.burinigor7.loginservice.internalcalls.UserFeignInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoginService {
    private final PasswordEncoder passwordEncoder;
    private final UserFeignInterface userClient;
    private final JwtTokenProvider jwtTokenProvider;

    public String login(LoginRequestDto requestDto) {
        Optional<UserTokenDataDto> tokenData =
                userClient.loadUserRole(requestDto.getLogin());
        if(tokenData.isPresent() && passwordEncoder.matches(requestDto.getPassword(),
                tokenData.get().getEncryptedPassword())) {
            return jwtTokenProvider.createToken(tokenData.get());
        }
        throw new RuntimeException("Login exception");
    }
}
