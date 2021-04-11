package com.example.board.repository;

import com.example.board.dto.SearchDTO;
import com.example.board.entity.Post;
import com.example.board.entity.PostCategory;
import com.example.board.entity.SearchType;
import com.example.board.util.QueryDslUtil;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

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
    public Page<Post> postSearchListPaging(SearchDTO searchDTO, Pageable pageable) {
        List<Post> postList = null;
        long total = 0L;
        SearchType searchType = SearchType.convertToType(searchDTO.getSearchType());
        switch (searchType) {
            case USER:
                postList = searchByUsernamePaging(searchDTO.getPostCategory(), searchDTO.getQuery(), pageable);
                total = searchByUsernamePagingTotal(searchDTO.getPostCategory(), searchDTO.getQuery(), pageable);
                break;
            case TITLE:
                postList = searchByPostTitlePaging(searchDTO.getPostCategory(), searchDTO.getQuery(), pageable);
                total = searchByPostTitlePagingTotal(searchDTO.getPostCategory(), searchDTO.getQuery(), pageable);
                break;
        }

        return new PageImpl<>(postList, pageable, total);
    }

    @Override
    public Page<Post> postList(PostCategory postCategory, Pageable pageable) {
        List<OrderSpecifier> orderSpecifiers = sortingCondition(pageable.getSort());

        List<Post> postList = queryFactory
                .selectFrom(post)
                .orderBy(orderSpecifiers.toArray(OrderSpecifier[]::new))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long totalCount = queryFactory
                .selectFrom(post)
                .orderBy(orderSpecifiers.toArray(OrderSpecifier[]::new))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchCount();

        return new PageImpl<>(postList, pageable, totalCount);

    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private List<OrderSpecifier> sortingCondition(Sort sort) {
        List<OrderSpecifier> orders = new ArrayList<>();

        if (!sort.isEmpty()) {
            for (Sort.Order order : sort) {
                Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;

                switch (order.getProperty()) {
                    case "popular":
                        OrderSpecifier<?> orderPopular = QueryDslUtil.getSortedColumn(direction, post, "likeCount");
                        orders.add(orderPopular);
                        break;
                    default:
                        OrderSpecifier<?> orderCreatedAt = QueryDslUtil.getSortedColumn(direction, post, "createdAt");
                        orders.add(orderCreatedAt);
                        break;
                }
            }
        }

        return orders;
    }

    private List<Post> searchByPostTitlePaging(PostCategory postCategory, String title, Pageable pageable) {
        return queryFactory
                .selectFrom(post)
                .where(post.title.contains(title), post.category.eq(postCategory))
                .orderBy(post.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    private Long searchByPostTitlePagingTotal(PostCategory postCategory, String title, Pageable pageable) {
        return queryFactory
                .selectFrom(post)
                .where(post.title.contains(title), post.category.eq(postCategory))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchCount();
    }

    private List<Post> searchByUsernamePaging(PostCategory postCategory, String userNickname, Pageable pageable) {
        return queryFactory
                .selectFrom(post)
                .join(post.user, user).fetchJoin()
                .where(user.name.contains(userNickname),
                        post.category.eq(postCategory))
                .offset(pageable.getOffset())
                .orderBy(post.createdAt.desc())
                .limit(pageable.getPageSize())
                .fetch();
    }

    private Long searchByUsernamePagingTotal(PostCategory postCategory, String userNickname, Pageable pageable) {
        return queryFactory
                .selectFrom(post)
                .join(post.user, user).fetchJoin()
                .where(user.name.contains(userNickname),
                        post.category.eq(postCategory))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchCount();
    }
}
