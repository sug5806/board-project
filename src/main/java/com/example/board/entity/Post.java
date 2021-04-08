package com.example.board.entity;

import com.example.board.entity.common.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseEntity {

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
    private int commentCount = 0;

    @Enumerated(value = EnumType.STRING)
    private PostCategory category;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(fetch = LAZY, mappedBy = "post")
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(fetch = LAZY, mappedBy = "post")
    private List<PostLike> postLikes = new ArrayList<>();

    public void changeTitle(String title) {
        this.title = title;
    }

    public void changeContents(String contents) {
        this.contents = contents;
    }

    public void amountViewCount() {
        this.viewCount = this.getViewCount() + 1;
    }

    public void mappingUser(User user) {
        this.user = user;
        user.mappingPost(this);
    }

    public void mappingComment(Comment comment) {
        this.comments.add(comment);
    }

    public void mappingPostLike(PostLike postLike) {
        this.postLikes.add(postLike);
    }

    public void updateLikeCount() {
        this.likeCount = (long) postLikes.size();
    }
}
