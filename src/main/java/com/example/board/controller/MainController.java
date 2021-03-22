package com.example.board.controller;

import com.example.board.dto.PostDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    @GetMapping("/")
    public String Main() {
        return "main";
    }

    @GetMapping("/board/post")
    public String createPostForm(Model model) {
        model.addAttribute("form", PostDTO.builder().build());
        return "/board/form";
    }


}
