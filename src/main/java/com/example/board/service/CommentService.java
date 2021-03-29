package com.example.board.service;

import com.example.board.dto.CommentDTO;
import com.example.board.entity.Comment;
import com.example.board.entity.Post;
import com.example.board.entity.User;
import com.example.board.repository.CommentRepository;
import com.example.board.repository.PostRepository;
import com.example.board.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public CommentDTO createComment(Long postId, Principal principal, CommentDTO commentDTO) {
        Post findPost = postRepository.getOne(postId);
        Optional<User> byEmail = userRepository.findByEmail(principal.getName());

        User user = byEmail.orElseThrow(() -> new UsernameNotFoundException("해당 유저가 존재하지 않습니다."));

        Comment comment = Comment.builder()
                .contents(commentDTO.getContents())
                .post(findPost)
                .user(user)
                .build();

        comment.mappingPostAndUser(findPost, user);

        Comment createdComment = commentRepository.save(comment);

        return CommentDTO.builder()
                .contents(createdComment.getContents())
                .build();
    }
}
