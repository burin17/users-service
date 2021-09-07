package com.gmail.burinigor7.loginservice.service;

import com.gmail.burinigor7.loginservice.dto.UserTokenDataDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Base64;
import java.util.Date;

@Service
public class JwtTokenProvider {
    @Value("${jwt.token.limit}")
    private long tokenLimit;

    @Value("${jwt.token.secret}")
    private String secret;

    @PostConstruct
    public void encodeSecret() {
        secret = Base64.getEncoder().encodeToString(secret.getBytes());
    }


    public String createToken(UserTokenDataDto user) {
        Claims claims = Jwts.claims().setSubject(user.getLogin());
        claims.put("role", user.getRole().getTitle());
        Date now = new Date();
        Date expired = new Date(now.getTime() + tokenLimit);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expired)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }
}
