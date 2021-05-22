package com.example.board.entity.economy_video;

import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
public class ScheduleChannel {

    @Id
    @GeneratedValue
    private Long id;
    private String channelName;
    private String channelUrl;
}
