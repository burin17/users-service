package com.gmail.burinigor7.usersservice.dao;

import com.gmail.burinigor7.usersservice.domain.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

}
