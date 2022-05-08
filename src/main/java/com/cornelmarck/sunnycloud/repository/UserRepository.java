package com.cornelmarck.sunnycloud.repository;

import com.cornelmarck.sunnycloud.model.User;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

@EnableScan
public interface UserRepository extends CrudRepository<User, User.PrimaryKey> {
}
