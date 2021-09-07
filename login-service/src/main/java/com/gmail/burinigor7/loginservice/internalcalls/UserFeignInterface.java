package com.gmail.burinigor7.loginservice.internalcalls;

import com.gmail.burinigor7.loginservice.dto.UserTokenDataDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.Optional;

@FeignClient(name = "users-crud-service")
@Service
public interface UserFeignInterface {
    @GetMapping("/admin/users")
    Optional<UserTokenDataDto> loadUserRole(@RequestParam("login") String login);
}
