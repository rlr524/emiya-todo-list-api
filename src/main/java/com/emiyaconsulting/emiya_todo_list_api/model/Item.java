package com.emiyaconsulting.emiya_todo_list_api.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collation = "items")
public class Item {
    @Id
    private String id;
    private String title;
}
