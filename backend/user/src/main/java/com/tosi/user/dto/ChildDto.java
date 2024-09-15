package com.tosi.user.dto;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChildDto {

    private String childName;
    private int childGender;

    @Builder
    public ChildDto(String childName, int childGender) {
        this.childName = childName;
        this.childGender = childGender;
    }
}
