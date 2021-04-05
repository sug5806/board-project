package com.example.board.service;

import com.example.board.config.custom_exception.PostNotFoundException;
import com.example.board.dto.CommentDTO;
import com.example.board.dto.PostDTO;
import com.example.board.dto.UserDTO;
import com.example.board.entity.*;
import com.example.board.repository.PostRepository;
import com.example.board.repository.UserRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.management.OperationsException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.example.board.entity.QPost.post;
import static com.example.board.entity.QUser.user;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    private final QPost qPost = post;
    private final QUser qUser = user;
    @PersistenceContext
    private EntityManager entityManager;
    private JPAQueryFactory queryFactory;

    @PostConstruct
    private void init() {
        queryFactory = new JPAQueryFactory(entityManager);
    }

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
        List<Post> postList = queryFactory
                .selectFrom(qPost)
                .where(qPost.category.eq(category))
                .orderBy(qPost.id.desc())
                .fetch();

        return convertToPostDTOList(postList);
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

    public List<PostDTO> postSearchList(PostCategory postCategory, String target, String title) {
        List<Post> postList = new ArrayList<>();

        if (target.equals("title")) {
            postList = searchByPostTitle(postCategory, title);
        } else if (target.equals("user")) {
            postList = searchByUsername(postCategory, title);
        }

        return convertToPostDTOList(postList);
    }

    private List<Post> searchByPostTitle(PostCategory postCategory, String title) {
        return queryFactory
                .selectFrom(qPost)
                .where(qPost.title.contains(title), qPost.category.eq(postCategory))
                .fetch();

    }

    private List<Post> searchByUsername(PostCategory postCategory, String userNickname) {
        return queryFactory
                .selectFrom(qPost)
                .join(qPost.user, qUser).fetchJoin()
                .where(qUser.name.eq(userNickname))
                .where(qPost.category.eq(postCategory))
                .fetch();
    }

    private List<PostDTO> convertToPostDTOList(List<Post> postList) {
        Stream<Post> stream = postList.stream();

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
