package com.example.board.common;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Sort;

@Getter
@Setter
public class PageRequest {
    private static final int DEFAULT_PAGE_SIZE = 10;
    private static final int MAXIMUM_PAGE_SIZE = 50;

    private int page = 1;
    private int size = 15;
    private Sort.Direction direction = Sort.Direction.DESC;
    private String orderBy = "createdAt";

    public org.springframework.data.domain.PageRequest of() {
        return org.springframework.data.domain.PageRequest.of(page - 1, size, direction, orderBy);
    }
}
