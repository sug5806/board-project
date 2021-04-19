package com.example.board.controller.error;

import com.example.board.common.custom_exception.PostNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends RuntimeException {

    @ExceptionHandler(PostNotFoundException.class)
    public String PostNotFoundException(PostNotFoundException e) {
        return "error/post_not_found.html";
    }
}
