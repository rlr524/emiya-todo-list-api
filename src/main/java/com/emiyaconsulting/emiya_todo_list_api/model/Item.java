package com.emiyaconsulting.emiya_todo_list_api.model;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@Document("items")
public class Item {
    @Id
    private String id;
    private String title;
    private String itemDescription;
    private String due;
    private boolean complete;
    private String importance;
    private String owner;
    private boolean deleted;
    private LocalDateTime deletedAt;
    @CreatedDate
    private Date createdAt;
    @LastModifiedDate
    private Date updatedAt;

    public Item(String id, String title, String itemDescription, String due, String importance, String owner, Boolean deleted, Boolean complete) {
        this.id = id;
        this.title = title;
        this.itemDescription = itemDescription;
        this.due = due;
        this.complete = false;
        this.importance = "Medium";
        this.owner = owner;
        this.deleted = false;
    }
}



