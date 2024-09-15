package com.tosi.user.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ExceptionCode {
    INVAILD_TOKEN(HttpStatus.BAD_REQUEST, "USER_001", "유효하지 않은 토큰입니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER_002", "회원을 찾을 수 없습니다.");


    private final HttpStatus status;
    private final String code;
    private final String message;

}
