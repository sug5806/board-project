package com.example.board.controller.post;

import com.example.board.dto.PostDTO;
import com.example.board.entity.Post;
import com.example.board.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.management.OperationsException;
import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/board/post")
    public String createPostForm(Model model, @RequestParam(name = "board", defaultValue = "free") String boardType) {
        model.addAttribute("form", PostDTO.builder().build());
        model.addAttribute("type", boardType);
        model.addAttribute("is_create", true);
        return "post/create";
    }

    @PostMapping("/post")
    @PreAuthorize("isAuthenticated()")
    public String createPost(@Valid PostDTO postDTO, Principal principal) {
        Post post = postService.createPost(postDTO, principal);
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
        PostDTO postDTO = postService.getPost(id);
        model.addAttribute("post", postDTO);
        model.addAttribute("type", postDTO.getCategory());

        return "post/post";

    }

    @GetMapping("/post/{id}/edit")
    public String editPost(@PathVariable(name = "id") Long id, Model model) {
        PostDTO postDTO = postService.getPost(id);
        model.addAttribute("form", postDTO);

        return "post/edit";
    }

    @PostMapping("/post/{id}")
    @PreAuthorize("isAuthenticated()")
    public String updatePost(@PathVariable(name = "id") Long id, @Valid PostDTO postDTO) {
        Post post = postService.updatePost(id, postDTO);

        return "redirect:/post/" + post.getId();
    }

    @PostMapping("/post/{id}/delete")
    @PreAuthorize("isAuthenticated()")
    public String deletePost(@PathVariable(name = "id") Long id) {
        String postCategory = "";
        try {
            postCategory = postService.deletePost(id);
        } catch (OperationsException e) {
            return "main";
        }

        return "redirect:/board/" + postCategory;
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
