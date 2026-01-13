package com.emiyaconsulting.emiya_todo_list_api.exception;

import java.util.logging.Logger;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
