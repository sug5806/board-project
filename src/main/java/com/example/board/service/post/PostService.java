package com.example.board.service.post;

import com.example.board.common.custom_exception.PostNotFoundException;
import com.example.board.dto.post.PostDTO;
import com.example.board.dto.post.search.SearchDTO;
import com.example.board.entity.Post;
import com.example.board.entity.PostCategory;
import com.example.board.entity.PostLike;
import com.example.board.entity.User;
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
import java.util.Optional;

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

    public Page<PostDTO> postListPaging(PostCategory category, SearchDTO searchDTO, Pageable pageable) {
        return postRepository.postList(category, searchDTO, pageable).map(PostDTO::convertToPostDTO);
    }

    @Transactional
    public PostDTO getPost(Long id, Principal principal) {
        Optional<Post> optionalPost = postRepository.findById(id);
        Post foundPost = optionalPost.orElseThrow(PostNotFoundException::new);

        Optional<PostLike> optionalPostLike = Optional.empty();

        if (principal != null) {
            Optional<User> optionalUser = userRepository.findByEmail(principal.getName());
            User user = optionalUser.orElseThrow(() -> new UsernameNotFoundException("올바르지 않은 유저입니다."));

            optionalPostLike = postLikeRepository.findByPostAndUser(foundPost, user);
        }

        foundPost.amountViewCount();

        return PostDTO.convertToPostDTO(foundPost, optionalPostLike);
    }


    @Transactional
    public PostDTO updatePost(Long id, PostDTO postDTO) {
        Post one = postRepository.getOne(id);

        one.changeTitle(postDTO.getTitle());
        one.changeContents(postDTO.getContents());

        return PostDTO.convertToPostDTO(one);
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

    @Transactional
    public void postLike(Long id, Principal principal) {

        Optional<Post> optionalPost = postRepository.findById(id);
        Post post = optionalPost.orElseThrow(() -> new PostNotFoundException(id));

        Optional<User> optionalUser = userRepository.findByEmail(principal.getName());
        User user = optionalUser.orElseThrow(() -> new UsernameNotFoundException("올바르지 않은 유저입니다."));

        Optional<PostLike> optionalPostLike = postLikeRepository.findByPostAndUser(post, user);

        optionalPostLike.ifPresentOrElse(
                postLike -> {
                    postLikeRepository.delete(postLike);
                    post.discountLike(postLike);
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
                }
        );
    }
}
