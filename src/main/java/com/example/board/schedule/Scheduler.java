package com.example.board.schedule;

import com.example.board.entity.economy_video.ScheduleChannel;
import com.example.board.repository.economy_video.ScheduleChannelRepository;
import com.example.board.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Component
@RequiredArgsConstructor
public class Scheduler {

    private final ScheduleChannelRepository scheduleChannelRepository;
    private static List<ScheduleChannel> URL_LIST = new ArrayList<>();
    private final ScheduleService scheduleService;

    @PostConstruct
    private void init() {
        URL_LIST = scheduleChannelRepository.findAll();
    }

    @Scheduled(cron = "* * * * * *")
    public void cronJob() throws ExecutionException, InterruptedException {

        for (ScheduleChannel scheduleChannel : URL_LIST) {
            scheduleService.economyVideoScheduling(scheduleChannel);
        }

    }
}
