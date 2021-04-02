package com.example.board.config.security.login;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
public class AuthFailureHandler implements AuthenticationFailureHandler {

    private final String DEFAULT_FAILURE_URL = "/login?error=true";

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String errorMessage = null;

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        if (email.isBlank() || password.isBlank()) {
            errorMessage = "아이디나 비밀번호를 입력해주세요.";
        } else if (exception instanceof BadCredentialsException || exception instanceof InternalAuthenticationServiceException) {
            errorMessage = "아이디나 비밀번호가 맞지 않습니다. 다시 확인해 주세요.";
        } else if (exception instanceof UsernameNotFoundException) {
            errorMessage = "유저가 존재하지 않습니다.";
        }

        request.setAttribute("email", email);
        request.setAttribute("password", password);
        request.setAttribute("errorMessage", errorMessage);

        request.getRequestDispatcher(DEFAULT_FAILURE_URL).forward(request, response);
    }
}
