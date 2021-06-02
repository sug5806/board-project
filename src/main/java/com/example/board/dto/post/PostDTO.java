package com.example.board.dto.post;

import com.example.board.dto.comment.CommentDTO;
import com.example.board.dto.user.UserDTO;
import com.example.board.entity.Comment;
import com.example.board.entity.Post;
import com.example.board.entity.PostCategory;
import com.example.board.entity.PostLike;
import lombok.*;

import javax.validation.constraints.NotBlank;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostDTO {
    public static final int SEC = 60;
    public static final int MIN = 60;
    public static final int HOUR = 24;
    public static final int DAY = 30;
    public static final int MONTH = 12;

    private Long id;

    @NotBlank(message = "제목을 입력해주세요")
    private String title;

    @NotBlank(message = "내용을 입력해주세요")
    private String contents;

    private PostCategory category;

    private Long viewCount;
    private Long likeCount;
    private int commentCount;
    private String creator;

    private UserDTO userDTO;

    private List<CommentDTO> comments;

    private String createdAt;

    private boolean isVoted;

    private static String formatTimeString(LocalDateTime time) {
        String msg;
        LocalDateTime currentTime = LocalDateTime.now();

        long seconds = Duration.between(time, currentTime).getSeconds();

        if (seconds < SEC) {
            msg = seconds + "초 전";
        } else if (seconds / SEC < MIN) {
            msg = seconds / SEC + "분 전";
        } else if (seconds / (SEC * MIN) < HOUR) {
            msg = seconds / (SEC * MIN) + "시간 전";
        } else if (seconds / (SEC * MIN * HOUR) < DAY) {
            msg = seconds / (SEC * MIN * HOUR) + "일 전";
        } else if (seconds / (SEC * MIN * HOUR * MONTH) < MONTH) {
            msg = seconds / (SEC * MIN * HOUR * MONTH) + "개월 전";
        } else {
            msg = seconds / (SEC * MIN * HOUR * MONTH) + "년 전";
        }

        return msg;
    }

    public static PostDTO convertToPostDTO(Post post) {
        return PostDTO.builder()
                .id(post.getId())
                .title(post.getTitle())
                .contents(post.getContents())
                .createdAt(formatTimeString(post.getCreatedAt()))
                .likeCount(post.getLikeCount())
                .viewCount(post.getViewCount())
                .category(post.getCategory())
                .userDTO(UserDTO.builder()
                        .email(post.getUser().getEmail())
                        .nickname(post.getUser().getName())
                        .build())
                .build();
    }

    public static PostDTO convertToPostDTO(Post post, Optional<PostLike> optionalPostLike) {
        Stream<Comment> stream = post.getComments().stream();

        List<CommentDTO> collect = CommentDTO.convertToCommentDto(stream);

        return PostDTO.builder()
                .id(post.getId())
                .title(post.getTitle())
                .contents(post.getContents())
                .createdAt(formatTimeString(post.getCreatedAt()))
                .likeCount(post.getLikeCount())
                .viewCount(post.getViewCount())
                .category(post.getCategory())
                .userDTO(UserDTO.builder()
                        .email(post.getUser().getEmail())
                        .nickname(post.getUser().getName())
                        .build())
                .comments(collect)
                .commentCount(collect.size())
                .isVoted(PostLike.isVotedPost(optionalPostLike))
                .build();
    }
}
