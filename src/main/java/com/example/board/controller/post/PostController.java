package com.example.board.controller.post;

import com.example.board.common.PageRequest;
import com.example.board.dto.PostDTO;
import com.example.board.dto.SearchDTO;
import com.example.board.entity.Post;
import com.example.board.entity.PostCategory;
import com.example.board.repository.PostRepository;
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
import java.util.List;

@Controller
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final PostRepository postRepository;

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
    public String postList(@PathVariable(name = "type") String boardType, Model model, PageRequest pageable) {
        Page<PostDTO> postListPaging = postService.postListPaging(PostCategory.convertToCategory(boardType), pageable.of());

        model.addAttribute("post_list", postListPaging);
        model.addAttribute("base_url", "http://localhost:8080");
        model.addAttribute("type", boardType);
        return "post/post_list";
    }

    @GetMapping("/board/{type}/search")
    public String postList(
            @PathVariable(name = "type") String boardType,
            @RequestParam(name = "target") String searchType,
            @RequestParam(name = "q") String query,
            Model model) {

        SearchDTO searchDTO = SearchDTO.builder()
                .postCategory(PostCategory.convertToCategory(boardType))
                .searchType(searchType)
                .query(query)
                .build();

        List<PostDTO> postList = postService.postSearchList(searchDTO);

        model.addAttribute("post_list", postList);
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
