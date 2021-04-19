package com.example.board.controller.post;

import com.example.board.common.PageRequest;
import com.example.board.dto.PostDTO;
import com.example.board.dto.SearchDTO;
import com.example.board.entity.Post;
import com.example.board.entity.PostCategory;
import com.example.board.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.management.OperationsException;
import javax.validation.Valid;
import java.security.Principal;

@Controller
@RequiredArgsConstructor
@Slf4j
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
    public String createPost(@Valid @ModelAttribute("form") PostDTO postDTO, BindingResult result, Model model, Principal principal) {
        if (result.hasErrors()) {
            model.addAttribute("is_create", true);
            return "post/create";
        }

        Post post = postService.createPost(postDTO, principal);
        model.addAttribute("is_create", true);

        return "redirect:/board/" + post.getCategory().toString().toLowerCase();
    }

    @GetMapping("/board/{type}")
    public String postList(
            @PathVariable(name = "type") String boardType,
            @RequestParam(name = "type", defaultValue = "") String searchType,
            @RequestParam(name = "query", defaultValue = "") String query,
            Model model,
            PageRequest pageable) {

        SearchDTO searchDTO = SearchDTO.builder()
                .type(searchType)
                .query(query)
                .build();

        Page<PostDTO> postListPaging = postService.postListPaging(PostCategory.convertToCategory(boardType), searchDTO, pageable.of());

        model.addAttribute("post_list", postListPaging);
        model.addAttribute("type", boardType);
        return "post/post_list";
    }

    @GetMapping("/post/{id}")
    public String getPost(@PathVariable(name = "id") Long id, Model model, Principal principal) {
        PostDTO postDTO = postService.getPost(id, principal);
        model.addAttribute("post", postDTO);
        model.addAttribute("type", postDTO.getCategory());

        return "post/post";
    }

    @GetMapping("/post/{id}/edit")
    @PreAuthorize("isAuthenticated()")
    public String editPost(@PathVariable(name = "id") Long id, Model model, Principal principal) {
        PostDTO postDTO = postService.getPost(id, principal);
        model.addAttribute("form", postDTO);

        return "post/edit";
    }

    @PostMapping("/post/{id}")
    @PreAuthorize("isAuthenticated()")
    public String updatePost(@PathVariable(name = "id") Long id, @Valid @ModelAttribute("form") PostDTO postDTO, BindingResult result) {
        if (result.hasErrors()) {
            return "post/edit";
        }

        postService.updatePost(id, postDTO);

        return "redirect:/post/" + id.toString();
    }

    @PostMapping("/post/{id}/delete")
    @PreAuthorize("isAuthenticated()")
    public String deletePost(@PathVariable(name = "id") Long id) {
        try {
            postService.deletePost(id);
        } catch (OperationsException e) {
            return "main";
        }

        return "redirect:/post/" + id.toString();
    }

    @PostMapping("/post/{id}/like")
    @PreAuthorize("isAuthenticated()")
    public String postLike(@PathVariable(name = "id") Long postId, Principal principal) {
        postService.postLike(postId, principal);
        return "redirect:/post/" + postId.toString();
    }
}
