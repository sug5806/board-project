package com.example.board.entity.economy_video;

import lombok.Getter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
public class ScheduleChannel {

    @Id
    @GeneratedValue
    private Long id;
    private String channelName;
    private String channelUrl;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "scheduleChannel")
    private List<EconomyVideo> economyVideoList;
}
