package com.example.board.controller.error;

import com.example.board.config.custom_exception.PostNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends RuntimeException {

    @ExceptionHandler(PostNotFoundException.class)
    public void handlerException(HttpServletRequest request, HttpServletResponse response, PostNotFoundException exception) throws ServletException, IOException {
        request.getRequestDispatcher("/templates/error/404.html").forward(request, response);

    }
}
