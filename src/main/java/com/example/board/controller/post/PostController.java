package com.example.board.controller.post;

import com.example.board.dto.PostDTO;
import com.example.board.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping("/board/post")
    public String createPostForm(Model model, @RequestParam(name = "board", defaultValue = "free") String boardType) {
        model.addAttribute("form", PostDTO.builder().build());
        model.addAttribute("type", boardType);
        model.addAttribute("is_write", true);
        return "main";
    }

    @GetMapping("/free")
    public @ResponseBody
    List<PostDTO> postList(@RequestParam(
            value = "type",
            defaultValue = "new") String type) {
        return postService.allPost(type);
    }
}
