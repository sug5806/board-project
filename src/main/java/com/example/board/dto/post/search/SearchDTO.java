package com.example.board.dto.post.search;

import lombok.*;

@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class SearchDTO {
    private String type;
    private String query;
}
