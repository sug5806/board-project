package com.example.board.common.custom_exception;

public class UserNicknameExistException extends RuntimeException {
    public UserNicknameExistException() {
        super("해당 닉네임은 이미 사용중입니다.");
    }
}
