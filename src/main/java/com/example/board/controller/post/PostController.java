package com.example.board.controller.post;

import com.example.board.common.PageRequest;
import com.example.board.dto.PostDTO;
import com.example.board.dto.SearchDTO;
import com.example.board.entity.Post;
import com.example.board.entity.PostCategory;
import com.example.board.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
import java.util.Map;

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

        Page<PostDTO.ConvertToPostDTO> postListPaging = postService.postListPaging(PostCategory.convertToCategory(boardType), searchDTO, pageable.of());

        model.addAttribute("post_list", postListPaging);
        model.addAttribute("type", boardType);
        model.addAttribute("target", searchDTO.getType());
        model.addAttribute("query", searchDTO.getQuery());
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
    @PreAuthorize("isAuthenticated()")
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

    @PostMapping("/post/{id}/like")
    @PreAuthorize("isAuthenticated()")
    public String postLike(@PathVariable(name = "id") Long id,
                           Principal principal,
                           Model model) {

        Map<String, Boolean> map = postService.postLike(id, principal);

        model.addAttribute("is_voted", map.get("is_voted"));

        return "post/post :: #article-vote";
    }

    @PostMapping("/post/{id}/dislike")
    @PreAuthorize("isAuthenticated()")
    public String postDisLike(@PathVariable(name = "id") Long id,
                              Model model,
                              Principal principal) {
        postService.postDisLike(id, principal);

        return "post/post :: #vote-arrow";
    }
}
