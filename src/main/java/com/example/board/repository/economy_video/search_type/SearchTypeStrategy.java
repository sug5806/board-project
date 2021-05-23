package com.example.board.repository.economy_video.search_type;

import com.querydsl.core.types.dsl.BooleanExpression;

public interface SearchTypeStrategy {
    BooleanExpression search(String query);
}
