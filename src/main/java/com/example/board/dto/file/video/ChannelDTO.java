package com.example.board.dto.file.video;

import com.example.board.entity.economy_video.ScheduleChannel;
import lombok.*;

@Data
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class ChannelDTO {
    private String name;

    public static ChannelDTO convertToChannelDTO(ScheduleChannel channel) {
        return ChannelDTO.builder()
                .name(channel.getChannelName())
                .build();
    }
}
