package com.example.board.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String contents;

    @Builder.Default
    private Long viewCount = 0L;

    @Builder.Default
    private Long likeCount = 0L;

    @Builder.Default
    private Long commentCount = 0L;

    @Enumerated(value = EnumType.STRING)
    private PostCategory category;

    public void changeTitle(String title) {
        this.title = title;
    }

    public void changeContents(String contents) {
        this.contents = contents;
    }

    public void amountViewCount() {
        this.viewCount = this.getViewCount() + 1;
    }
}
