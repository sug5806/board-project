package com.example.board.service;

import com.example.board.dto.PostDTO;
import com.example.board.entity.Post;
import com.example.board.entity.PostCategory;
import com.example.board.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.management.OperationsException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.example.board.entity.PostCategory.*;

@Service
@RequiredArgsConstructor
@Transactional
public class PostService {
    private final PostRepository postRepository;

    @Transactional
    public Post createPost(PostDTO postDTO) {
        Post post = Post.builder()
                .title(postDTO.getTitle())
                .contents(postDTO.getContents())
                .category(postDTO.getCategory())
                .build();

        return postRepository.save(post);
    }

    public List<PostDTO> allPost(String category) {
        // TODO: 카테고리 ENUM으로 변경하기
        PostCategory convertCategory = convertCategory(category);

        List<Post> list = postRepository.findAllByCategory(convertCategory, Sort.by(Sort.Direction.DESC, "id"));

        Stream<Post> stream = list.stream();

        Stream<PostDTO> dtoStream = stream.map(post ->
                PostDTO.builder()
                        .id(post.getId())
                        .title(post.getTitle())
                        .contents(post.getContents())
                        .viewCount(post.getViewCount())
                        .likeCount(post.getLikeCount())
                        .commentCount(post.getCommentCount())
                        .creator("demo")
                        .build()
        );

        return dtoStream.collect(Collectors.toList());
    }

    @Transactional
    public PostDTO getPost(Long id) {
        Optional<Post> optionalPost = postRepository.findById(id);

        Post foundPost = optionalPost.orElseThrow(() -> new NoSuchElementException("not found post"));

        foundPost.amountViewCount();

        return PostDTO.builder()
                .id(foundPost.getId())
                .title(foundPost.getTitle())
                .contents(foundPost.getContents())
                .viewCount(foundPost.getViewCount())
                .likeCount(foundPost.getLikeCount())
                .commentCount(foundPost.getCommentCount())
                .category(foundPost.getCategory())
                .build();
    }


    @Transactional
    public Post updatePost(Long id, PostDTO postDTO) {
        Post one = postRepository.getOne(id);

        one.changeTitle(postDTO.getTitle());
        one.changeContents(postDTO.getContents());

        return postRepository.save(one);
    }

    @Transactional
    public String deletePost(Long id) throws OperationsException {
        Post post = postRepository.getOne(id);
        postRepository.delete(post);

        boolean isExist = postRepository.existsById(id);

        if (isExist) {
            throw new OperationsException("게시글 삭제에 실패하였습니다.");
        }

        return post.getCategory().toString().toLowerCase();
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
