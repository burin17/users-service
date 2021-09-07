package com.gmail.burinigor7.admindeletionservice.service;

import com.gmail.burinigor7.admindeletionservice.dao.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public boolean isDeletionAllowed(Long id) {
        return userRepository.existsById(id);
    }
}
