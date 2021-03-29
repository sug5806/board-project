package com.example.board.dto;

import lombok.*;

@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Data
public class CommentDTO {
    private String contents;
    private UserDTO user;
}
