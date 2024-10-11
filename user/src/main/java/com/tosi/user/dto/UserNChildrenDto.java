package com.tosi.user.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

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
