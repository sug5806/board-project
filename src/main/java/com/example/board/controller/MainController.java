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
        // TODO: 카테고리 ENUM으로 변경하기
        List<PostDTO> freePostList = postService.allPost("free");

        model.addAttribute("type", "free");
        model.addAttribute("post_list", freePostList);
        model.addAttribute("base_url", "http://localhost:8080");
        return "main";
    }
}
