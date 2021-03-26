package com.example.board.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostDTO {
    private Long id;

    @NotBlank
    private String title;

    @NotBlank
    private String contents;

    @NotBlank
    private String category;

    private Long viewCount;
    private Long likeCount;
    private String creator;
}
