package com.example.board.service;

import com.example.board.dto.SearchDTO;
import com.example.board.dto.video.VideoDTO;
import com.example.board.repository.economy_video.EconomyVideoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EconomyVideoService {

    private final EconomyVideoRepository economyVideoRepository;

    public Page<VideoDTO> videoListPaging(SearchDTO searchDTO, Pageable pageable) {
        return economyVideoRepository.videoListPagingQueryDSL(searchDTO, pageable).map(VideoDTO::convertToVideoDTO);
    }
}
