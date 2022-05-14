package com.cornelmarck.sunnycloud.controller;

import com.cornelmarck.sunnycloud.model.User;
import com.cornelmarck.sunnycloud.repository.UserRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {
    private final UserRepository repository;

    public UserController(UserRepository userRepository) {
        this.repository = userRepository;
    }

    @GetMapping("/users/{emailAddress}")
    User one(@PathVariable String emailAddress) {
        return repository.findByEmailAddress(emailAddress)
                .orElseThrow(() -> new UserNotFoundException(emailAddress));
    }

    @GetMapping("/users")
    List<User> all() {
        return repository.findAll();
    }
}
