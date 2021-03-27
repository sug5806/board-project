package com.example.board.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum PostCategory implements EnumMapperType {
    FREE("자유"),
    ECONOMY("경제"),
    STOCK("주식");

    @Getter
    private final String title;

    @Override
    public String getCode() {
        return name();
    }
}
