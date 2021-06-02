package com.example.board.controller.comment;

import com.example.board.dto.comment.CommentDTO;
import com.example.board.service.comment.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.security.Principal;

@Controller
@PreAuthorize("isAuthenticated()")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/post/{id}/comment")
    public String createComment(
            @Valid CommentDTO commentDTO,
            @PathVariable(name = "id") Long id,
            Principal principal) {

        CommentDTO comment = commentService.createComment(id, principal, commentDTO);

        return "redirect:/post/" + id.toString();
    }

    @PostMapping("/post/{post_id}/comment/{comment_id}/delete")
    public String deleteComment(
            @PathVariable(name = "post_id") Long postId,
            @PathVariable(name = "comment_id") Long commentId,
            Principal principal) {
        commentService.deleteComment(commentId, principal);

        return "redirect:/post/" + postId.toString();
    }
}
