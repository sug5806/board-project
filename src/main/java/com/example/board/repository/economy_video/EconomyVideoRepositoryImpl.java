package com.example.board.repository.economy_video;

import com.example.board.dto.SearchDTO;
import com.example.board.entity.SearchType;
import com.example.board.entity.economy_video.EconomyVideo;
import com.example.board.repository.economy_video.search_type.SearchTypeStrategy;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

import static com.example.board.entity.economy_video.QEconomyVideo.economyVideo;

@RequiredArgsConstructor
public class EconomyVideoRepositoryImpl implements EconomyVideoCustom {

    private final JPAQueryFactory queryFactory;
    private final Map<String, SearchTypeStrategy> searchTypeStrategyMap;

    @Override
    public Page<EconomyVideo> videoListPagingQueryDSL(SearchDTO searchDTO, Pageable pageable) {
        BooleanExpression searchQuery = postSearchQuery(searchDTO);


        List<EconomyVideo> economyVideoList = queryFactory
                .selectFrom(economyVideo)
                .where(searchQuery)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(economyVideo.createdAt.desc())
                .fetch();

        long count = queryFactory
                .selectFrom(economyVideo)
                .where(searchQuery)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(economyVideo.createdAt.desc())
                .fetchCount();


        return new PageImpl<>(economyVideoList, pageable, count);
    }

    private BooleanExpression postSearchQuery(SearchDTO searchDTO) {
        SearchType searchType = SearchType.convertToType(searchDTO.getType());

        if (searchDTO.getQuery() != null) {
            SearchTypeStrategy searchTypeStrategy = searchTypeStrategyMap.get(searchType.getType());

            return searchTypeStrategy.search(searchDTO.getQuery());
        }

        return null;

    }

}
