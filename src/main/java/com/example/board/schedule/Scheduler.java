package com.example.board.schedule;

import com.example.board.entity.economy_video.ScheduleChannel;
import com.example.board.repository.economy_video.ScheduleChannelRepository;
import com.example.board.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Component
@RequiredArgsConstructor
@Slf4j
public class Scheduler {

    private final ScheduleChannelRepository scheduleChannelRepository;
    private static List<ScheduleChannel> URL_LIST = new ArrayList<>();
    private final ScheduleService scheduleService;

    @PostConstruct
    private void init() {
        URL_LIST = scheduleChannelRepository.findAll();
    }

    @Scheduled(cron = "0 0 0/3 * * *")
    public void cronJob() throws ExecutionException, InterruptedException {
        log.info("크론 작업 시작 : " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        for (ScheduleChannel scheduleChannel : URL_LIST) {
            scheduleService.economyVideoScheduling(scheduleChannel);
        }

    }
}
