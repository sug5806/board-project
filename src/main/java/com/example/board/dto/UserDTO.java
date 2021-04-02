package com.example.board.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserDTO {
    private Long id;
    private String email;
    private String nickname;
}
