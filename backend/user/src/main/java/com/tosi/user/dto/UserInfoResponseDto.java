package com.tosi.user.dto;

import lombok.Data;

@Data
public class UserInfoResponseDto {

    private Integer userId;
    private String email;
    private String bookshelfName;
    //private List<Child> childrenList;

//    @Builder
//    public UserInfoResponse(String email, String bookshelfName, List<Child> childrenList) {
//        this.email = email;
//        this.bookshelfName = bookshelfName;
//        this.childrenList = childrenList;
//    }
}
