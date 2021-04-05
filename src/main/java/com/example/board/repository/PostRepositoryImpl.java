package com.example.board.repository;

import com.example.board.dto.PostDTO;
import com.example.board.dto.SearchDTO;
import com.example.board.dto.UserDTO;
import com.example.board.entity.Post;
import com.example.board.entity.PostCategory;
import com.example.board.entity.SearchType;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.example.board.entity.QPost.post;
import static com.example.board.entity.QUser.user;

public class PostRepositoryImpl implements PostRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;
    private JPAQueryFactory queryFactory;

    @PostConstruct
    private void init() {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public List<PostDTO> postSearchList(SearchDTO searchDTO) {
        List<Post> postList = null;
        SearchType searchType = SearchType.convertToType(searchDTO.getSearchType());
        switch (searchType) {
            case USER:
                postList = searchByUsername(searchDTO.getPostCategory(), searchDTO.getQuery());
                break;
            case TITLE:
                postList = searchByPostTitle(searchDTO.getPostCategory(), searchDTO.getQuery());
                break;
        }

        return convertToPostDTOList(postList);
    }

    @Override
    public List<PostDTO> postList(PostCategory postCategory) {
        List<Post> postList = queryFactory
                .selectFrom(post)
                .where(post.category.eq(postCategory))
                .orderBy(post.id.desc())
                .fetch();

        return convertToPostDTOList(postList);
    }

    private List<Post> searchByPostTitle(PostCategory postCategory, String title) {
        return queryFactory
                .selectFrom(post)
                .where(post.title.contains(title), post.category.eq(postCategory))
                .fetch();

    }

    private List<Post> searchByUsername(PostCategory postCategory, String userNickname) {
        return queryFactory
                .selectFrom(post)
                .join(post.user, user).fetchJoin()
                .where(user.name.contains(userNickname))
                .where(post.category.eq(postCategory))
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
}
