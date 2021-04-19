package com.example.board.controller.error;

import com.example.board.common.custom_exception.PostNotFoundException;
import com.example.board.common.custom_exception.UserEmailExistException;
import com.example.board.common.custom_exception.UserNicknameExistException;
import com.example.board.dto.SignupDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends RuntimeException {

    @ExceptionHandler(PostNotFoundException.class)
    public String PostNotFoundException(PostNotFoundException e) {
        log.error(e.getMessage());
        return "error/post_not_found.html";
    }

    @ExceptionHandler({UserEmailExistException.class, UserNicknameExistException.class})
    public String UserEmailExistException(HttpServletRequest req, Exception e, Model model) {
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String nickname = req.getParameter("nickname");

        model.addAttribute("form", SignupDTO.builder()
                .email(email)
                .password(password)
                .nickname(nickname)
                .build());

        if (e instanceof UserEmailExistException) {
            model.addAttribute("email_error_message", e.getMessage());
            model.addAttribute("nickname_error_message", "");
        } else if (e instanceof UserNicknameExistException) {
            model.addAttribute("email_error_message", "");
            model.addAttribute("nickname_error_message", e.getMessage());
        }

        return "error/email_exist.html";
    }
}


