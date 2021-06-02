package com.example.board.dto.comment;

import com.example.board.dto.user.UserDTO;
import com.example.board.entity.Comment;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Data
public class CommentDTO {
    private Long id;
    private String contents;
    private UserDTO user;
    private CommentDTO childComments;

    public static List<CommentDTO> convertToCommentDto(Stream<Comment> stream) {
        Stream<CommentDTO> commentDTOStream = stream.map(comment ->
                CommentDTO.builder()
                        .id(comment.getId())
                        .contents(comment.getContents())
                        .user(UserDTO.builder()
                                .id(comment.getUser().getId())
                                .email(comment.getUser().getEmail())
                                .nickname(comment.getUser().getName())
                                .build())
                        .build()
        );

        return commentDTOStream.collect(Collectors.toList());
    }
}
