package com.tosi.user.service;

import com.tosi.user.common.JWT.TokenInfo;
import com.tosi.user.common.exception.SuccessResponse;
import com.tosi.user.dto.*;

public interface UserService {
    SuccessResponse join(JoinDto joinDto);

    TokenInfo login(LoginDto loginDto);

    UserDto findUserDto(Long userId);

    UserNChildrenDto findUserNChildren(Long userId);

    SuccessResponse updateUser(Long userId, ModifyingUserDto modifyingUserDto);

    SuccessResponse addChild(Long userId, ChildDto childDto);

    SuccessResponse deleteChild(Long userId, Long childId);

}
