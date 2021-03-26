package com.example.board.service;

import com.example.board.dto.PostDTO;
import com.example.board.entitiy.Post;
import com.example.board.entitiy.PostCategory;
import com.example.board.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.example.board.entitiy.PostCategory.*;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    public Post createPost(PostDTO postDTO) {
        Post post = Post.builder()
                .title(postDTO.getTitle())
                .contents(postDTO.getContents())
                .build();

        post.setCategory(postDTO.getCategory());

        Post savePost = postRepository.save(post);

        return savePost;
    }

    public List<PostDTO> allPost(String category) {
        PostCategory convertCategory = convertCategory(category);

        List<Post> list = postRepository.findAllByCategory(convertCategory, Sort.by(Sort.Direction.DESC, "id"));

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

    private PostCategory convertCategory(String category) {
        switch (category) {
            case "economy":
                return ECONOMY;
            case "stock":
                return STOCK;
            default:
                return FREE;
        }
    }
}
