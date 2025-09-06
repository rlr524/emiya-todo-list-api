package com.emiyaconsulting.emiya_todo_list_api.repository;

import com.emiyaconsulting.emiya_todo_list_api.model.Item;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends MongoRepository<Item, String> {
    public long count();
}
