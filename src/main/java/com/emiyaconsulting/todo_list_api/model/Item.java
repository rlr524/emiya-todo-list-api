package com.emiyaconsulting.todo_list_api.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor
@EqualsAndHashCode
@Document(collection = "items")
public class Item {
    @Id
    private String id;
    @NotNull
    private String title;
    private String itemDescription;
    private LocalDate due;
    private boolean complete;
    private String importance;
    @NotNull
    private String owner; // User ID of the user owner of the item
    @NotNull
    private boolean deleted;
    private Instant deletedAt;
    @CreatedDate
    private Instant createdAt;
    @LastModifiedDate
    private Instant updatedAt;
}



