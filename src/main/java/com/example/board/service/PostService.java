package com.example.board.service;

import com.example.board.config.custom_exception.PostNotFoundException;
import com.example.board.dto.CommentDTO;
import com.example.board.dto.PostDTO;
import com.example.board.dto.SearchDTO;
import com.example.board.dto.UserDTO;
import com.example.board.entity.Comment;
import com.example.board.entity.Post;
import com.example.board.entity.PostCategory;
import com.example.board.entity.User;
import com.example.board.repository.PostRepository;
import com.example.board.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.management.OperationsException;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
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

    public List<PostDTO> postList(PostCategory category) {
        return postRepository.postList(category);
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

    public List<PostDTO> postSearchList(SearchDTO searchDTO) {
        return postRepository.postSearchList(searchDTO);

    }

    private PostDTO convertToPostDTO(Post foundPost) {
        Stream<Comment> stream = foundPost.getComments().stream();

        List<CommentDTO> collect = getComment(stream);

        return PostDTO.builder()
                .id(foundPost.getId())
                .title(foundPost.getTitle())
                .contents(foundPost.getContents())
                .viewCount(foundPost.getViewCount())
                .likeCount(foundPost.getLikeCount())
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
}
