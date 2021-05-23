package com.example.board.repository.economy_video.search_type;

import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.stereotype.Component;

import static com.example.board.entity.economy_video.QScheduleChannel.scheduleChannel;

@Component("user")
public class SearchUser implements SearchTypeStrategy {
    @Override
    public BooleanExpression search(String query) {
        return scheduleChannel.channelName.eq(query);
    }
}
