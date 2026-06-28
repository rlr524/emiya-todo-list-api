package com.emiyaconsulting.todo_list_api.repository;

import com.emiyaconsulting.todo_list_api.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByUserName(String userName);
}
