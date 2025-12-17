package com.emiyaconsulting.emiya_todo_list_api.exception.handler;

import com.emiyaconsulting.emiya_todo_list_api.exception.ItemNotFoundException;
import com.emiyaconsulting.emiya_todo_list_api.exception.UserNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ToDoListGlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(value = {ItemNotFoundException.class})
    public ResponseEntity<?> handleItemNotFound(ItemNotFoundException itemNotFoundException, WebRequest request) {
        return handleExceptionInternal(itemNotFoundException, itemNotFoundException.getMessage(), new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }
    
    @ExceptionHandler(value = {UserNotFoundException.class})
    public ResponseEntity<?> handleUserNotFound(UserNotFoundException userNotFoundException, WebRequest request) {
        return handleExceptionInternal(userNotFoundException, userNotFoundException.getMessage(), new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }
}
