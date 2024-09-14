package com.tosi.user.service;

import com.tosi.user.dto.UserInfoRequest;
import com.tosi.user.common.exception.SuccessResponse;

public interface UserService {
    SuccessResponse postUser(UserInfoRequest userInfoRequest);
}
