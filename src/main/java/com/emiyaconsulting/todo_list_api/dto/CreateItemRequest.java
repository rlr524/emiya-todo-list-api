package com.emiyaconsulting.todo_list_api.dto;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;

public record CreateItemRequest (
    @NotBlank String title,
    String itemDescription,
    LocalDate due,
    String importance
    ) {}
