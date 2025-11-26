package com.emiyaconsulting.emiya_todo_list_api.repository;

import com.emiyaconsulting.emiya_todo_list_api.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
}
