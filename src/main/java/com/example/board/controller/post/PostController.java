package com.example.board.controller.post;

import com.example.board.dto.PostDTO;
import com.example.board.entitiy.Post;
import com.example.board.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping("/board/post")
    public String createPostForm(Model model, @RequestParam(name = "board", defaultValue = "free") String boardType) {
        model.addAttribute("form", PostDTO.builder().build());
        model.addAttribute("type", boardType);
        return "post/create";
    }

    @PostMapping("/post")
    public String createPost(@Valid PostDTO postDTO) {
        Post post = postService.createPost(postDTO);
        return "redirect:/board/" + post.getCategory().toString().toLowerCase();
    }

    @GetMapping("/board/{type}")
    public String postList(@PathVariable(name = "type") String boardType, Model model) {
        List<PostDTO> postList = postService.allPost(boardType);

        model.addAttribute("post_list", postList);
        model.addAttribute("base_url", "http://localhost:8080");
        model.addAttribute("type", boardType);
        return "post/post_list";
    }

    @GetMapping("/post/{id}")
    public String getPost(@PathVariable(name = "id") Long id, Model model) {
        PostDTO post = postService.getPost(id);
        model.addAttribute("post", post);
        model.addAttribute("type", post.getCategory().toLowerCase());

        return "post/post";

    }

//    @GetMapping("/board/{type}")
//    public String postList(@PathVariable(name = "type") String boardType, Model model) {
//        List<PostDTO> postList = postService.allPost(boardType);
//
//        model.addAttribute("post_list", postList);
////        model.addAttribute("base_url", "http://localhost:8080");
//        model.addAttribute("type", boardType);
//        return "main :: #pList";
//    }
}
