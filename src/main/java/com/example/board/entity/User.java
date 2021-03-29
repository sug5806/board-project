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

    private String email;

    @Column(length = 100)
    private String name;

    private String password;

    private String role;

    private String principal;

    @OneToMany(fetch = LAZY, mappedBy = "user")
    private List<Post> postList = new ArrayList<>();

    @OneToMany(fetch = LAZY, mappedBy = "user")
    private List<Comment> comments = new ArrayList<>();

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
}

