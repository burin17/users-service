package com.gmail.burinigor7.apigatewayservice.internalcalls;

import com.gmail.burinigor7.apigatewayservice.dto.UserAuthDataDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.Optional;

@FeignClient(name = "users-crud-service")
@Service
public interface UserFeignInterface {
    @GetMapping("/admin/users")
    Optional<UserAuthDataDto> fetchUserByLogin(@RequestParam String login);

    @DeleteMapping("/admin/users/{deletedId}/{currentId}")
    void deleteUser(@PathVariable Long deletedId, @PathVariable Long currentId);
}
