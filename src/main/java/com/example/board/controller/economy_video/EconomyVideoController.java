package com.example.board.controller.economy_video;

import com.example.board.common.PageRequest;
import com.example.board.dto.file.video.ChannelDTO;
import com.example.board.dto.file.video.VideoDTO;
import com.example.board.dto.post.search.SearchDTO;
import com.example.board.service.video.ChannelService;
import com.example.board.service.video.EconomyVideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@Controller
@RequiredArgsConstructor
public class EconomyVideoController {

    private final EconomyVideoService economyVideoService;
    private final ChannelService channelService;

    @GetMapping("/board/economy-video")
    public String EconomyVideo(
            @RequestParam(name = "type", defaultValue = "title") String searchType,
            @RequestParam(name = "query", defaultValue = "") String query,
            PageRequest pageable,
            Model model
    ) {

        SearchDTO searchDTO = SearchDTO.builder()
                .type(searchType)
                .query(query)
                .build();

        Page<VideoDTO> videoList = economyVideoService.videoListPaging(searchDTO, pageable.of());
        List<ChannelDTO> channelList = channelService.getAllChannel();

        model.addAttribute("post_list", videoList);
        model.addAttribute("channel_list", channelList);

        return "economy-video/video";
    }
}
