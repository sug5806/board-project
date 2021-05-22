package com.example.board.repository.economy_video;

import com.example.board.entity.economy_video.ScheduleChannel;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScheduleChannelRepository extends JpaRepository<ScheduleChannel, Long> {

    @Override
    @EntityGraph(attributePaths = {"economyVideoList"})
    List<ScheduleChannel> findAll();
}
