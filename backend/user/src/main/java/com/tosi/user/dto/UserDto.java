package com.tosi.user.dto;

import com.querydsl.core.annotations.QueryProjection;
import jakarta.persistence.Column;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.OffsetDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserDto {

    private Long userId;
    private String email;
    private String nickname;
    private String bookshelfName;

    @Builder
    @QueryProjection
    public UserDto(Long userId, String email, String nickname, String bookshelfName) {
        this.userId = userId;
        this.email = email;
        this.nickname = nickname;
        this.bookshelfName = bookshelfName;
    }

}


