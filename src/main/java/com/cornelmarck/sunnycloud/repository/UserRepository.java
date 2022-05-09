package com.cornelmarck.sunnycloud.repository;

import com.cornelmarck.sunnycloud.model.User;
import com.cornelmarck.sunnycloud.model.UserPrimaryKey;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

@EnableScan
public interface UserRepository extends CrudRepository<User, UserPrimaryKey> {
    Optional<User> findUserByEmailAddress(String emailAddress);
}
