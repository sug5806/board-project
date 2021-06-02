package com.example.board.entity;

import com.example.board.entity.common.DateTimeEntity;
import lombok.*;
import org.springframework.security.crypto.bcrypt.BCrypt;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class User extends DateTimeEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(length = 100)
    private String name;

    private String password;

    @Column
    private String picture;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

//    private String principal;

    @OneToMany(fetch = LAZY, mappedBy = "user")
    private List<Post> postList = new ArrayList<>();

    @OneToMany(fetch = LAZY, mappedBy = "user")
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(fetch = LAZY, mappedBy = "user")
    private List<PostLike> postLikes = new ArrayList<>();

    public void clearPassword() {
        this.password = null;
    }

    public void createAndSetPassword(String rawPassword) {
        this.password = BCrypt.hashpw(rawPassword, BCrypt.gensalt());
    }

    public void mappingPost(Post post) {
        this.postList.add(post);
    }

    public void mappingComment(Comment comment) {
        this.comments.add(comment);
    }

    public void mappingPostLike(PostLike postLike) {
        this.postLikes.add(postLike);
    }

    public void passwordEncryption(String password) {
        this.password = BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public User update(String name, String picture) {
        this.name = name;
        this.picture = picture;

        return this;
    }

    public String getRoleKey() {
        return this.role.getKey();
    }
}

