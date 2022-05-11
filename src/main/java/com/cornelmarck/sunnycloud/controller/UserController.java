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

    @GetMapping("/users/{id}")
    User one(@PathVariable String id) {
        return repository.findByIdAndSortKeyEquals(id, "UserDetails")
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    @GetMapping("/users")
    List<User> all() {
        return repository.findAllBySortKeyEquals("UserDetails");
    }
}
