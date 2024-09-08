package com.tosi.user.jwt.service;

import com.ssafy.tosi.jwt.JwtUtil;
import com.ssafy.tosi.jwt.RefreshToken;
import com.ssafy.tosi.jwt.RefreshTokenRepository;
import com.ssafy.tosi.user.UserService;
import com.ssafy.tosi.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class TokenService {

    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;
    private final RefreshTokenService refreshTokenService;
    private final UserService userService;


    public Map generateNewAccessToken(String refreshToken) {

        if(!jwtUtil.validateToken(refreshToken)) {
            return null;
        }
        Integer userId = jwtUtil.getUserId(refreshToken);
//        Integer userId = refreshTokenService.findByRefreshToken(refreshToken).getUserId();
        System.out.println("tokenService_userId: " + userId);
        User user = userService.selectUser(userId);

        String newAccessToken = jwtUtil.generateToken(userId, Duration.ofDays(1));
        String newRefreshToken = jwtUtil.generateToken(userId, Duration.ofDays(7));
        RefreshToken refreshTokenEntity = RefreshToken.builder().userId(userId).refreshToken(newRefreshToken).build();
        refreshTokenService.updateRefreshToken(refreshTokenEntity);

        Map<String, String> map = new HashMap<>();
        map.put("access-token", newAccessToken);
        map.put("refresh-token", newRefreshToken);

        return map;
    }



}
