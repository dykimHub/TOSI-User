package com.tosi.user.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserDto {

    private Long userId;
    private String email;
    private String nickname;
    private String bookshelfName;

    @QueryProjection
    public UserDto(Long userId, String email, String nickname, String bookshelfName) {
        this.userId = userId;
        this.email = email;
        this.nickname = nickname;
        this.bookshelfName = bookshelfName;
    }

}


