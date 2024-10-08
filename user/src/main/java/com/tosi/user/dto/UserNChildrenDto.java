package com.tosi.user.dto;

import lombok.*;

import java.util.List;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserNChildrenDto {

    private UserDto userDto;
    private List<ChildDto> children;

    @Builder
    public UserNChildrenDto(UserDto userDto, List<ChildDto> children) {
        this.userDto = userDto;
        this.children = children;
    }
}
