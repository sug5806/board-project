package com.example.board.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@RequiredArgsConstructor
public enum PostCategory implements EnumMapperType {
    FREE("자유", "free"),
    ECONOMY("경제", "economy"),
    STOCK("주식", "stock");

    @Getter
    private final String title;

    @Getter
    private final String category;

    public static PostCategory convertToCategory(String category) {
        return Arrays.stream(values())
                .filter(postCategory -> postCategory.category.equals(category))
                .findAny()
                .orElse(FREE);
    }
}
