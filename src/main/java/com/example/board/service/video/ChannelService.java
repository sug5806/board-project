package com.example.board.service.video;

import com.example.board.dto.file.video.ChannelDTO;
import com.example.board.repository.economy_video.ScheduleChannelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChannelService {

    private final ScheduleChannelRepository channelRepository;

    public List<ChannelDTO> getAllChannel() {
        return channelRepository.findAll().stream().map(ChannelDTO::convertToChannelDTO).collect(Collectors.toList());
    }
}
