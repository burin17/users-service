package com.gmail.burinigor7.userscrudservice.internalcalls;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "admin-deletion-service")
@Service
public interface AdminDeletionFeignInterface {
    @GetMapping("/api/users/is-allowed/{id}")
    boolean isAllowed(@PathVariable Long id);
}
