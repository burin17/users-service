package com.gmail.burinigor7.userscrudservice.api;

import com.gmail.burinigor7.userscrudservice.exception.AdminDeletionServiceNotAccessibleException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class AdminDeletionApi {
    private final RestTemplate restTemplate;
    private Set<Long> cache = new HashSet<>();

    @Value("${admin-deletion-service.url.check}")
    private String url;

    @Retryable(value = ResourceAccessException.class)
    public boolean isAllowed(Long id) {
        boolean res = Boolean.TRUE.equals(restTemplate.getForObject(url, boolean.class, id));
        if (res) {
            cache.add(id);
        } else {
            cache.remove(id);
        }
        return res;
    }

    @Recover
    public boolean isAllowedRecover(ResourceAccessException exception,
                                    String url, Long id) {
        if (cache.contains(id)) {
            return true;
        }
        throw new AdminDeletionServiceNotAccessibleException("Requested api not accessible.");
    }

    Set<Long> getCache() {
        return cache;
    }
}
