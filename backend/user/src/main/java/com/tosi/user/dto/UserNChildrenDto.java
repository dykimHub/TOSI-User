package com.tosi.user.dto;

import lombok.*;

import java.util.List;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserNChildrenDto {

    private String nickname;
    private String bookshelfName;
    private List<ChildDto> children;

    @Builder
    public UserNChildrenDto(Long userId, String nickname, String bookshelfName, List<ChildDto> children) {
        this.nickname = nickname;
        this.bookshelfName = bookshelfName;
        this.children = children;
    }
}
