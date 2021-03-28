package com.example.board.controller.login;

import com.example.board.dto.LoginDTO;
import com.example.board.dto.SignupDTO;
import com.example.board.dto.UserDTO;
import com.example.board.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class LoginController {

    private final UserService userService;

    @GetMapping("/login")
    public String loginForm(Model model) {
        model.addAttribute("form", LoginDTO.builder().build());

        return "/login";
    }

    @GetMapping("/signup")
    public String singupForm(Model model) {
        model.addAttribute("form", SignupDTO.builder().build());

        return "/signup";
    }

    @PostMapping("/signup")
    public String singup(@Valid SignupDTO signupDTO) {
        UserDTO signup = userService.signup(signupDTO);


        return "redirect:/";
    }
}
