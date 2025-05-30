package com.tosi.user.JWT;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum JwtExpiration {
    ACCESS_TOKEN_EXPIRATION_TIME("JWT 만료 시간 / 2시간", 1000L * 60 * 60 * 2),
    REFRESH_TOKEN_EXPIRATION_TIME("Refresh 토큰 만료 시간 / 7일", 1000L * 60 * 60 * 24 * 7),
    REISSUE_EXPIRATION_TIME("Refresh 토큰 재발급 시간 / 3일", 1000L * 60 * 60 * 24 * 3);

    private String description;
    private Long value;
}
