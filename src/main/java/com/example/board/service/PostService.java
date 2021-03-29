package com.example.board.service;

import com.example.board.dto.CommentDTO;
import com.example.board.dto.PostDTO;
import com.example.board.dto.UserDTO;
import com.example.board.entity.Comment;
import com.example.board.entity.Post;
import com.example.board.entity.PostCategory;
import com.example.board.entity.User;
import com.example.board.repository.PostRepository;
import com.example.board.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.management.OperationsException;
import java.security.Principal;
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
    private final UserRepository userRepository;

    @Transactional
    public Post createPost(PostDTO postDTO, Principal principal) {

        Optional<User> byEmail = userRepository.findByEmail(principal.getName());

        User user = byEmail.orElseThrow(() -> new UsernameNotFoundException("아이디나 비밀번호가 틀립니다."));

        Post post = Post.builder()
                .title(postDTO.getTitle())
                .contents(postDTO.getContents())
                .category(postDTO.getCategory())
                .build();

        post.mappingUser(user);

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
                        .userDTO(UserDTO.builder()
                                .id(post.getUser().getId())
                                .nickname(post.getUser().getName())
                                .build())
                        .build()
        );

        return dtoStream.collect(Collectors.toList());
    }

    @Transactional
    public PostDTO getPost(Long id) {
        Optional<Post> optionalPost = postRepository.findById(id);

        Post foundPost = optionalPost.orElseThrow(() -> new NoSuchElementException("not found post"));

        foundPost.amountViewCount();

        Stream<Comment> stream = foundPost.getComments().stream();

        Stream<CommentDTO> commentDTOStream = stream.map(comment ->
                CommentDTO.builder()
                        .contents(comment.getContents())
                        .user(UserDTO.builder()
                                .id(comment.getUser().getId())
                                .nickname(comment.getUser().getName())
                                .build())
                        .build()
        );

        List<CommentDTO> collect = commentDTOStream.collect(Collectors.toList());

        return PostDTO.builder()
                .id(foundPost.getId())
                .title(foundPost.getTitle())
                .contents(foundPost.getContents())
                .viewCount(foundPost.getViewCount())
                .likeCount(foundPost.getLikeCount())
                .category(foundPost.getCategory())
                .userDTO(UserDTO.builder()
                        .nickname(foundPost.getUser().getName())
                        .build())
                .comments(collect)
                .commentCount(collect.size())
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
