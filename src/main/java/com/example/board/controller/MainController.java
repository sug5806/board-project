package com.example.board.controller;

import com.example.board.dto.PostDTO;
import com.example.board.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final PostService postService;

    @GetMapping("/")
    public String Main(Model model) {
        List<PostDTO> freePostList = postService.allPost("free");

        model.addAttribute("type", "free");
        model.addAttribute("post_list", freePostList);
        return "main";
    }
}
