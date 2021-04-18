package com.example.board.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Optional;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn
    private User user;

    @ManyToOne(fetch = LAZY)
    @JoinColumn
    private Post post;

    public void mappingUser(User user) {
        this.user = user;
        user.mappingPostLike(this);
    }

    public void mappingPost(Post post) {
        this.post = post;
        post.mappingPostLike(this);
    }

    public static boolean isVotedPost(Optional<PostLike> optionalPostLike) {
        return optionalPostLike.isPresent();
    }

}
