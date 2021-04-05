package com.example.board.dto;

import com.example.board.entity.PostCategory;
import lombok.*;

@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class SearchDTO {
    private String searchType;

    private String query;

    private PostCategory postCategory;
}
