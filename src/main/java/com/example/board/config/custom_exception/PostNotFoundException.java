package com.example.board.config.custom_exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PostNotFoundException extends RuntimeException {

    public PostNotFoundException(Long id) {
        super("해당 포스트를 찾을 수 없습니다.");
        log.error(id + " 번 포스트를 찾을 수 없습니다!");
    }
}
