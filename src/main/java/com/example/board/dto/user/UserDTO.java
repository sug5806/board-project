package com.example.board.dto.user;

import com.example.board.entity.User;
import lombok.*;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserDTO {
    private Long id;
    private String email;
    private String nickname;

    public User toUser() {
        return User.builder().build();
    }

    @Getter
    public static class Res {
        private Long id;
        private String email;
        private String nickname;

        public Res(User user) {
            this.id = user.getId();
            this.email = user.getEmail();
            this.nickname = user.getName();
        }
    }
}
