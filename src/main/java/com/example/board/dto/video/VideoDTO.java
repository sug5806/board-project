package com.example.board.dto.video;

import com.example.board.entity.economy_video.EconomyVideo;
import lombok.*;

@Data
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class VideoDTO {

    private Long id;
    private String title;
    private String url;
    private String thumbUrl;
    private ChannelDTO channel;

    public static VideoDTO convertToVideoDTO(EconomyVideo economyVideo) {
        return VideoDTO.builder()
                .id(economyVideo.getId())
                .title(economyVideo.getTitle())
                .url(economyVideo.getUrl())
                .thumbUrl(economyVideo.getThumbnailUrl())
                .channel(ChannelDTO.convertToChannelDTO(economyVideo.getScheduleChannel()))
                .build();
    }
}
