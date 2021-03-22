package com.example.board.service;

import com.example.board.dto.PostDTO;
import com.example.board.entitiy.Post;
import com.example.board.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    public Long createPost(PostDTO postDTO) {
        Post post = Post.builder()
                .title(postDTO.getTitle())
                .contents(postDTO.getContents())
                .build();

        Post savePost = postRepository.save(post);

        return savePost.getId();
    }
}
