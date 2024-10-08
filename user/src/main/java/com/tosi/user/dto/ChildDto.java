package com.tosi.user.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChildDto {

    private Long childId;
    private String childName;
    private int childGender;

    @QueryProjection
    public ChildDto(Long childId, String childName, int childGender) {
        this.childId = childId;
        this.childName = childName;
        this.childGender = childGender;
    }
}
