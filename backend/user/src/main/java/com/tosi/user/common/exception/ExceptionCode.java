package com.tosi.user.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ExceptionCode {
    INVALID_TOKEN(HttpStatus.BAD_REQUEST, "USER_001", "유효하지 않은 토큰입니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER_002", "회원을 찾을 수 없습니다."),
    EXISTED_EMAIL(HttpStatus.BAD_REQUEST, "USER_003", "이미 존재하는 이메일 입니다."),
    EXISTED_NICKNAME(HttpStatus.BAD_REQUEST, "USER_004", "이미 존재하는 닉네임 입니다."),

    CHILDREN_NOT_FOUND(HttpStatus.NOT_FOUND, "CHILD_001", "회원의 자녀를 찾을 수 없습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

}
