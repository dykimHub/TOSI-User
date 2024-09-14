package com.tosi.user.dto;


import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserInfoRequest {

    private String email;
    private String password;
    private String userNickname;
    private String bookshelfName;

    public UserInfoRequest(String email, String password, String userNickname, String bookshelfName) {
        this.email = email;
        this.password = password;
        this.userNickname = userNickname;
        this.bookshelfName = bookshelfName;
    }
}
