package com.example.board.service;

import com.example.board.config.custom_exception.PostNotFoundException;
import com.example.board.dto.CommentDTO;
import com.example.board.dto.PostDTO;
import com.example.board.dto.SearchDTO;
import com.example.board.dto.UserDTO;
import com.example.board.entity.*;
import com.example.board.repository.PostLikeRepository;
import com.example.board.repository.PostRepository;
import com.example.board.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.management.OperationsException;
import java.security.Principal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.example.board.dto.PostDTO.ConvertToPostDTO.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PostLikeRepository postLikeRepository;

    @Transactional
    public Post createPost(PostDTO postDTO, Principal principal) {

        Optional<User> byEmail = userRepository.findByEmail(principal.getName());

        User user = byEmail.orElseThrow(() -> new UsernameNotFoundException(""));

        Post post = Post.builder()
                .title(postDTO.getTitle())
                .contents(postDTO.getContents())
                .category(postDTO.getCategory())
                .build();

        post.mappingUser(user);

        return postRepository.save(post);
    }

    public Page<PostDTO.ConvertToPostDTO> postListPaging(PostCategory category, Pageable pageable) {
        return postRepository.postList(category, pageable).map(PostDTO.ConvertToPostDTO::new);
    }

    @Transactional
    public PostDTO getPost(Long id) {
        Optional<Post> optionalPost = postRepository.findById(id);

        Post foundPost = optionalPost.orElseThrow(() -> new PostNotFoundException(id));

        foundPost.amountViewCount();

        return convertToPostDTO(foundPost);
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

    public Page<PostDTO.ConvertToPostDTO> postSearchListPaging(SearchDTO searchDTO, Pageable pageable) {
        return postRepository.postSearchListPaging(searchDTO, pageable).map(PostDTO.ConvertToPostDTO::new);
    }

    @Transactional
    public Map<String, Boolean> postLike(Long id, Principal principal) {
        Map<String, Boolean> map = new HashMap<>();

        Optional<Post> optionalPost = postRepository.findById(id);
        Post post = optionalPost.orElseThrow(() -> new PostNotFoundException(id));

        Optional<User> optionalUser = userRepository.findByEmail(principal.getName());
        User user = optionalUser.orElseThrow(() -> new UsernameNotFoundException("유저 에러"));

        Optional<PostLike> optionalPostLike = postLikeRepository.findByPostAndUser(post, user);
        optionalPostLike.ifPresentOrElse(
                postLike -> {
                },
                () -> {
                    PostLike postLike = PostLike.builder()
                            .post(post)
                            .user(user)
                            .build();

                    postLike.mappingPost(post);
                    postLike.mappingUser(user);

                    post.updateLikeCount();

                    postLikeRepository.save(postLike);

                    map.put("is_voted", true);
                });

        return map;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private PostDTO convertToPostDTO(Post foundPost) {
        Stream<Comment> stream = foundPost.getComments().stream();

        List<CommentDTO> collect = getComment(stream);

        return PostDTO.builder()
                .id(foundPost.getId())
                .title(foundPost.getTitle())
                .contents(foundPost.getContents())
                .createdAt(formatTimeString(foundPost.getCreatedAt()))
                .likeCount(foundPost.getLikeCount())
                .viewCount(foundPost.getViewCount())
                .category(foundPost.getCategory())
                .userDTO(UserDTO.builder()
                        .email(foundPost.getUser().getEmail())
                        .nickname(foundPost.getUser().getName())
                        .build())
                .comments(collect)
                .commentCount(collect.size())
                .build();
    }

    private List<CommentDTO> getComment(Stream<Comment> stream) {
        Stream<CommentDTO> commentDTOStream = stream.map(comment ->
                CommentDTO.builder()
                        .id(comment.getId())
                        .contents(comment.getContents())
                        .user(UserDTO.builder()
                                .id(comment.getUser().getId())
                                .email(comment.getUser().getEmail())
                                .nickname(comment.getUser().getName())
                                .build())
                        .build()
        );

        return commentDTOStream.collect(Collectors.toList());
    }

    private String formatTimeString(LocalDateTime time) {
        String msg = "";
        LocalDateTime currentTime = LocalDateTime.now();

        long seconds = Duration.between(time, currentTime).getSeconds();

        if (seconds < SEC) {
            msg = seconds + "초 전";
        } else if (seconds / SEC < MIN) {
            msg = seconds / SEC + "분 전";
        } else if (seconds / (SEC * MIN) < HOUR) {
            msg = seconds / (SEC * MIN) + "시간 전";
        } else if (seconds / (SEC * MIN * HOUR) < DAY) {
            msg = seconds / (SEC * MIN * HOUR) + "일 전";
        } else if (seconds / (SEC * MIN * HOUR * MONTH) < MONTH) {
            msg = seconds / (SEC * MIN * HOUR * MONTH) + "개월 전";
        } else {
            msg = seconds / (SEC * MIN * HOUR * MONTH) + "년 전";
        }

        return msg;
    }


    public void postDisLike(Long id, Principal principal) {
    }
}
