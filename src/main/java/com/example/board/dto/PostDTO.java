package com.example.board.dto;

import com.example.board.entity.Post;
import com.example.board.entity.PostCategory;
import lombok.*;

import javax.validation.constraints.NotBlank;
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

    @Getter
    public static class ConvertToPostDTO {
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

        public ConvertToPostDTO(Post post) {
            this.id = post.getId();
            this.title = post.getTitle();
            this.category = post.getCategory();
            this.viewCount = post.getViewCount();
            this.likeCount = post.getLikeCount();
            this.commentCount = post.getCommentCount();
            this.creator = post.getCreatedBy();
            this.userDTO = UserDTO.builder()
                    .nickname(post.getUser().getName())
                    .build();
        }
    }
}
