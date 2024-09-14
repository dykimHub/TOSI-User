package com.tosi.user.dto;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserInfoRequestDto {

    private String email;
    private String password;
    private String userNickname;
    private String bookshelfName;
    private List<ChildInfoDto> childInfoDtoList;

    public UserInfoRequestDto(String email, String password, String userNickname, String bookshelfName, List<ChildInfoDto> childInfoDtoList) {
        this.email = email;
        this.password = password;
        this.userNickname = userNickname;
        this.bookshelfName = bookshelfName;
        this.childInfoDtoList = childInfoDtoList;
    }
}
