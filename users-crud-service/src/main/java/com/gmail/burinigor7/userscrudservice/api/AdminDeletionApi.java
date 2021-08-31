package com.gmail.burinigor7.userscrudservice.api;

import lombok.RequiredArgsConstructor;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class AdminDeletionApi {
    private final RestTemplate restTemplate;

    @Retryable(backoff = @Backoff(delay = 4000), value = Exception.class, maxAttempts = 5)
    public boolean isAllowed(String url, Long id) {
        System.out.println("isAllowed()");
        return Boolean.TRUE.equals(restTemplate.getForObject(url, boolean.class, id));
    }
}
