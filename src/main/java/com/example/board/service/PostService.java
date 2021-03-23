package com.example.board.service;

import com.example.board.dto.PostDTO;
import com.example.board.entitiy.Post;
import com.example.board.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    public List<PostDTO> postList(String type) {
        List<Post> list = postRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));

        Stream<Post> stream = list.stream();

        Stream<PostDTO> dtoStream = stream.map(post ->
                PostDTO.builder()
                        .title(post.getTitle())
                        .contents(post.getContents())
                        .viewCount(post.getViewCount())
                        .creator("demo")
                        .likeCount(post.getLikeCount())
                        .build()
        );

        return dtoStream.collect(Collectors.toList());
    }
}
