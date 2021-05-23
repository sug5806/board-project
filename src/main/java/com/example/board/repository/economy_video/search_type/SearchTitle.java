package com.example.board.repository.economy_video.search_type;

import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.stereotype.Component;

import static com.example.board.entity.economy_video.QEconomyVideo.economyVideo;

@Component("title")
public class SearchTitle implements SearchTypeStrategy {
    @Override
    public BooleanExpression search(String query) {
        return economyVideo.title.contains(query);
    }
}
