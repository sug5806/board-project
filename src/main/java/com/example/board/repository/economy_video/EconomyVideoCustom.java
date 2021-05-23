package com.example.board.repository.economy_video;

import com.example.board.dto.SearchDTO;
import com.example.board.entity.economy_video.EconomyVideo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EconomyVideoCustom {
    Page<EconomyVideo> videoListPagingQueryDSL(SearchDTO searchDTO, Pageable pageable);
}
