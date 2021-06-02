package com.example.board.controller.login;

import com.example.board.dto.user.login.LoginDTO;
import com.example.board.dto.user.signup.SignupDTO;
import com.example.board.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class LoginController {

    private final UserService userService;

    @GetMapping("/login")
    public String loginForm(@ModelAttribute("form") LoginDTO loginDTO) {
        return "login";
    }

    @PostMapping("/login")
    public String loginFail(@ModelAttribute("form") LoginDTO loginDTO) {
        return "login";
    }

    @GetMapping("/signup")
    public String singupForm(@ModelAttribute("form") SignupDTO signupDTO) {
        return "signup";
    }

    @PostMapping("/signup")
    public String singup(@Valid @ModelAttribute("form") SignupDTO signupDTO, BindingResult result) {
        if (result.hasErrors()) {
            return "signup";
        }

        userService.signup(signupDTO);

        return "redirect:/";
    }
}
