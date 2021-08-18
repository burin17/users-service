package com.gmail.burinigor7.usersservice.dao;

import com.gmail.burinigor7.usersservice.domain.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface RoleRepository extends CrudRepository<Role,Long> {
    Optional<Role> findByTitle(String title);
}
