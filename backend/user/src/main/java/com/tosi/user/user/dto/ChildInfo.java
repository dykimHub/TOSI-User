package com.tosi.user.user.dto;

import com.ssafy.tosi.user.entity.Child;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ChildInfo {

    private String childName;
    private Integer userId;
    private int gender;
    private boolean myBaby;

    public Child toEntity(Integer userId) {
        return Child.builder()
                .userId(userId)
                .childName(childName)
                .gender(gender)
                .myBaby(myBaby)
                .build();
    }
}
