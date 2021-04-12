package com.example.board.dto;

import com.example.board.entity.Post;
import com.example.board.entity.PostCategory;
import lombok.*;

import javax.validation.constraints.NotBlank;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

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

    private PostCategory category;

    private Long viewCount;
    private Long likeCount;
    private int commentCount;
    private String creator;

    private UserDTO userDTO;

    private List<CommentDTO> comments;

    private String createdAt;

    @Getter
    public static class ConvertToPostDTO {
        public static final int SEC = 60;

        public static final int MIN = 60;

        public static final int HOUR = 24;

        public static final int DAY = 30;

        public static final int MONTH = 12;

        private Long id;

        private String title;

        private Long likeCount;

        private PostCategory category;

        private UserDTO userDTO;

        private String createdAt;

        public ConvertToPostDTO(Post post) {
            this.id = post.getId();
            this.title = post.getTitle();
            this.category = post.getCategory();
            this.likeCount = post.getLikeCount();
            this.createdAt = formatTimeString(post.getCreatedAt());
            this.userDTO = UserDTO.builder()
                    .nickname(post.getUser().getName())
                    .build();
        }

        private String formatTimeString(LocalDateTime time) {
            String msg = "";
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
    }
}
