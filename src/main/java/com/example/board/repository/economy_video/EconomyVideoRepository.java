package com.example.board.repository.economy_video;

import com.example.board.entity.economy_video.EconomyVideo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EconomyVideoRepository extends JpaRepository<EconomyVideo, Long> {
}
