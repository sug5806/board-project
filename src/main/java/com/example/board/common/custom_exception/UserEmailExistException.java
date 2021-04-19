package com.example.board.common.custom_exception;

public class UserEmailExistException extends RuntimeException {

    public UserEmailExistException() {
        super("해당 이메일은 이미 사용중입니다.");
    }
}
