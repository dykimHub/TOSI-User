package com.tosi.user.service;

import com.tosi.user.common.JWT.TokenInfo;
import com.tosi.user.common.exception.SuccessResponse;
import com.tosi.user.dto.LoginDto;
import com.tosi.user.entity.User;

public interface AuthService {

    TokenInfo findTokenInfo(LoginDto loginDto, User user);

    String findUserAuthorization(String accessToken);

    SuccessResponse invalidateToken(TokenInfo tokenInfo, String email);

    TokenInfo reissue(String refreshToken);
}
