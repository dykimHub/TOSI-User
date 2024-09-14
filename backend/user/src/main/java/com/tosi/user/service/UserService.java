package com.tosi.user.service;

import com.tosi.user.common.exception.SuccessResponse;
import com.tosi.user.dto.UserInfoRequestDto;

public interface UserService {
    SuccessResponse addUser(UserInfoRequestDto userInfoRequestDto);

    boolean findUserEmailDuplication(String email);

    boolean findUserNickNameDuplication(String nick);
}
