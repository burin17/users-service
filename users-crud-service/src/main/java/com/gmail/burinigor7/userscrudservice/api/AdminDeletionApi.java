package com.gmail.burinigor7.userscrudservice.api;

import com.gmail.burinigor7.userscrudservice.exception.AdminDeletionServiceNotAccessibleException;
import com.gmail.burinigor7.userscrudservice.internalcalls.AdminDeletionFeignInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class AdminDeletionApi {
    private final AdminDeletionFeignInterface adminDeletionClient;
    private Set<Long> cache = new HashSet<>();

    @Retryable(value = ResourceAccessException.class)
    public boolean isAllowed(Long id) {
        boolean res = adminDeletionClient.isAllowed(id);
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
