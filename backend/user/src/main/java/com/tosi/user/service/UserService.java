package com.tosi.user.service;

import com.tosi.user.common.JWT.TokenInfo;
import com.tosi.user.common.exception.SuccessResponse;
import com.tosi.user.dto.*;

public interface UserService {
    SuccessResponse join(JoinDto joinDto);

    TokenInfo login(LoginDto loginDto);

    SuccessResponse logout(TokenInfo tokenInfo, String email);

    SuccessResponse findUserEmailDuplication(String email);

    SuccessResponse findUserNickNameDuplication(String nickname);

    UserDto findUserDto(String accessToken);

    UserNChildrenDto findUserNChildren(UserDto userDto);

    SuccessResponse updateUser(UserDto modifyingUserDto);

    SuccessResponse addChild(Long userId, ChildDto childDto);

    SuccessResponse deleteChild(Long userId, Long childId);

}
