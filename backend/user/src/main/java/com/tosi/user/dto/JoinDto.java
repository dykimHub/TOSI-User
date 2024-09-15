package com.tosi.user.dto;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class JoinDto {

    private String email;
    private String password;
    private String nickname;
    private String bookshelfName;
    private List<ChildDto> children;

}
