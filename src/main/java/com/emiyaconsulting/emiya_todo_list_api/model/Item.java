package com.emiyaconsulting.emiya_todo_list_api.model;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor
@EqualsAndHashCode
@Document("items")
public class Item {
    @Id
    private String id;
    private String title;
    private String itemDescription;
    private LocalDate due;
    private boolean complete;
    private String importance;
    private String owner;
    private boolean deleted;
    private Instant deletedAt;
    @CreatedDate
    private Instant createdAt;
    @LastModifiedDate
    private Instant updatedAt;
}



