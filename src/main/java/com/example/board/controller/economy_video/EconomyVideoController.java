package com.example.board.controller.economy_video;

import com.example.board.common.PageRequest;
import com.example.board.dto.SearchDTO;
import com.example.board.dto.video.VideoDTO;
import com.example.board.service.EconomyVideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class EconomyVideoController {

    private final EconomyVideoService economyVideoService;

    @GetMapping("/board/economy-video")
    public String EconomyVideo(
            @RequestParam(name = "query", defaultValue = "") String query,
            PageRequest pageable,
            Model model
    ) {

        SearchDTO searchDTO = SearchDTO.builder()
                .query(query)
                .build();

        Page<VideoDTO> videoList = economyVideoService.videoListPaging(searchDTO, pageable.of());

        model.addAttribute("post_list", videoList);

        return "economy-video/video";
    }
}
