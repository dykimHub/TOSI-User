package com.tosi.user.service;

import com.tosi.user.common.JWT.TokenInfo;
import com.tosi.user.common.exception.SuccessResponse;
import com.tosi.user.dto.LoginDto;
import com.tosi.user.dto.JoinDto;
import com.tosi.user.dto.UserDto;
import com.tosi.user.dto.UserNChildrenDto;

public interface UserService {
    SuccessResponse addUser(JoinDto joinDto);

    boolean findUserEmailDuplication(String email);

    boolean findUserNickNameDuplication(String nickname);

    TokenInfo findUser(LoginDto loginDto);

    UserNChildrenDto findUserChildren(String userId);

    UserDto findUserDto(String accessToken);
}
