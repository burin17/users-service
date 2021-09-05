package com.gmail.burinigor7.loginservice.controller;

import com.gmail.burinigor7.loginservice.dto.LoginRequestDto;
import com.gmail.burinigor7.loginservice.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class LoginController {
    private final LoginService loginService;

    @GetMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto requestDto) {
        String token = loginService.login(requestDto);
        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        response.put("login", requestDto.getLogin());
        return ResponseEntity.ok(response);
    }
}
