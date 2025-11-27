package com.emiyaconsulting.emiya_todo_list_api.dto;

public class UserNotCreatedException extends RuntimeException {
    public UserNotCreatedException(String id) {
        super("User could not be created due to an error");
    }
}
