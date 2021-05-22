package com.example.board.entity.economy_video;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
public class EconomyVideo {

    @Id
    @GeneratedValue
    private Long id;
    private String title;
    private String url;
    private String thumbnailUrl;
    private LocalDateTime createdAt;
}
