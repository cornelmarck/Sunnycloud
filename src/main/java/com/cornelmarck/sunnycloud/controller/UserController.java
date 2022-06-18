package com.cornelmarck.sunnycloud.controller;

import com.cornelmarck.sunnycloud.model.User;
import com.cornelmarck.sunnycloud.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class UserController {
    private final UserRepository repository;

    public UserController(UserRepository userRepository) {
        this.repository = userRepository;
    }

    @GetMapping("/users/{id}")
    User one(@PathVariable String id) {
        return repository.findByEmailAddress(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found: " + id));
    }

    @PutMapping("/users/{id}")
    public User replaceUser(@PathVariable String id, @RequestBody User newUser) {
        return repository.findByEmailAddress(id)
                .map(user -> {
                    user.setName(newUser.getName());
                    user.setMobilePhoneNumber(newUser.getMobilePhoneNumber());
                    repository.save(user);
                    return user;
                })
                .orElseGet(() -> {
                    newUser.setEmailAddress(id);
                    repository.save(newUser);
                    return newUser;
                });
    }


}
