package com.example.board.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostDTO {
    private String title;
    private String contents;
    private Long viewCount;
}
