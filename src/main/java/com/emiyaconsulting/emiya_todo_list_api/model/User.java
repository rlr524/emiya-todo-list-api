package com.emiyaconsulting.emiya_todo_list_api.model;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.util.Set;

@Getter @Setter @NoArgsConstructor
@EqualsAndHashCode
@Document("users")
public class User {
    @Id
    private String id;
    @Field("first_name")
    private String firstName;
    @NonNull
    @Field("last_name")
    private String lastName;
    @NonNull
    @Field("user_name")
    private String userName;
    @NonNull
    private String email;
    private boolean active;
    private boolean deleted;
    private Instant deletedAt;
    @CreatedDate
    private Instant createdAt;
    @LastModifiedDate
    private Instant updatedAt;
}
