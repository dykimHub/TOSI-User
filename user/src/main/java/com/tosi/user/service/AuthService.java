package com.tosi.user.service;

import com.tosi.common.exception.SuccessResponse;
import com.tosi.user.JWT.TokenInfo;
import com.tosi.user.dto.LoginDto;
import com.tosi.user.entity.User;

public interface AuthService {

    TokenInfo findTokenInfo(LoginDto loginDto, User user);

    Long findUserAuthorization(String accessToken);

    SuccessResponse invalidateToken(TokenInfo tokenInfo, String email);

    TokenInfo reissue(String refreshToken);
}
