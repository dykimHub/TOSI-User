package com.tosi.user.service;

import com.tosi.user.common.JWT.TokenInfo;
import com.tosi.user.common.exception.SuccessResponse;
import com.tosi.user.dto.*;

public interface UserService {
    SuccessResponse addUser(JoinDto joinDto);

    SuccessResponse findUserEmailDuplication(String email);

    SuccessResponse findUserNickNameDuplication(String nickname);

    TokenInfo findUser(LoginDto loginDto);

    UserDto findUserDto(String accessToken);

    UserNChildrenDto findUserNChildren(UserDto userDto);

    SuccessResponse updateUser(UserDto modifyingUserDto);

    SuccessResponse addChild(Long userId, ChildDto childDto);

    SuccessResponse deleteChild(Long userId, Long childId);
}
