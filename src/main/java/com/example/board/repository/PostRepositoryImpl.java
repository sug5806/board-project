package com.example.board.repository;

import com.example.board.dto.SearchDTO;
import com.example.board.entity.Post;
import com.example.board.entity.PostCategory;
import com.example.board.entity.SearchType;
import com.example.board.util.QueryDslUtil;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.Builder;
import lombok.Getter;
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
    public Page<Post> postList(PostCategory postCategory, SearchDTO searchDTO, Pageable pageable) {

        return search(postCategory, searchDTO, pageable);
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private Page<Post> search(PostCategory postCategory, SearchDTO searchDTO, Pageable pageable) {
        PostListPaging postListPaging;

        List<OrderSpecifier> orderSpecifiers = sortingCondition(pageable.getSort());
        SearchType searchType = SearchType.convertToType(searchDTO.getType());

        PostListQueryCollect postListQueryCollect = PostListQueryCollect.builder()
                .OrderCondition(orderSpecifiers)
                .searchDTO(searchDTO)
                .postCategory(postCategory)
                .pageable(pageable)
                .build();

        switch (searchType) {
            case USER:
                postListPaging = searchByUsernamePaging(postListQueryCollect);
                break;
            default:
                postListPaging = searchByOther(postListQueryCollect);
                break;
        }

        return new PageImpl<>(postListPaging.postList, pageable, postListPaging.postTotalCount);
    }

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

    private PostListPaging searchByUsernamePaging(PostListQueryCollect qc) {
        List<Post> postList = queryFactory
                .selectFrom(post)
                .join(post.user, user).fetchJoin()
                .where(user.name.contains(qc.getSearchDTO().getQuery()),
                        post.category.eq(qc.getPostCategory()))
                .offset(qc.getPageable().getOffset())
                .orderBy(qc.getOrderCondition().toArray(OrderSpecifier[]::new))
                .limit(qc.getPageable().getPageSize())
                .fetch();

        long count = queryFactory
                .selectFrom(post)
                .join(post.user, user).fetchJoin()
                .where(user.name.contains(qc.getSearchDTO().getQuery()),
                        post.category.eq(qc.getPostCategory()))
                .offset(qc.getPageable().getOffset())
                .orderBy(qc.getOrderCondition().toArray(OrderSpecifier[]::new))
                .limit(qc.getPageable().getPageSize())
                .fetchCount();

        return PostListPaging.builder()
                .postList(postList)
                .postTotalCount(count)
                .build();
    }


    private PostListPaging searchByOther(PostListQueryCollect qc) {

        List<Post> postList = queryFactory
                .selectFrom(post)
                .where(post.title.contains(qc.getSearchDTO().getQuery()), post.category.eq(qc.getPostCategory()))
                .orderBy(qc.getOrderCondition().toArray(OrderSpecifier[]::new))
                .offset(qc.getPageable().getOffset())
                .limit(qc.getPageable().getPageSize())
                .fetch();

        long count = queryFactory
                .selectFrom(post)
                .where(post.title.contains(qc.getSearchDTO().getQuery()), post.category.eq(qc.getPostCategory()))
                .orderBy(qc.getOrderCondition().toArray(OrderSpecifier[]::new))
                .offset(qc.getPageable().getOffset())
                .limit(qc.getPageable().getPageSize())
                .fetchCount();

        return PostListPaging.builder()
                .postList(postList)
                .postTotalCount(count)
                .build();
    }

    @Builder
    @Getter
    private static class PostListPaging {
        private List<Post> postList;
        private Long postTotalCount;
    }

    @Builder
    @Getter
    private static class PostListQueryCollect {
        private List<OrderSpecifier> OrderCondition;
        private PostCategory postCategory;
        private SearchDTO searchDTO;
        private Pageable pageable;
    }
}


