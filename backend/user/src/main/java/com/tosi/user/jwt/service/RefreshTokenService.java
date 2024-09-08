package com.tosi.user.jwt.service;

import com.ssafy.tosi.jwt.JwtUtil;
import com.ssafy.tosi.jwt.RefreshToken;
import com.ssafy.tosi.jwt.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtil jwtUtil;

    public RefreshToken findByRefreshToken(String refreshToken) {
        return refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected token"));
    }


    public void updateRefreshToken(RefreshToken refreshToken) {
        Optional<RefreshToken> optionalRefreshToken = refreshTokenRepository.findByUserId(refreshToken.getUserId());

        if(optionalRefreshToken.isPresent()) {
            RefreshToken foundRefreshToken = optionalRefreshToken.get();

            foundRefreshToken.update(refreshToken);
            refreshTokenRepository.save(foundRefreshToken);
          } else {
            refreshTokenRepository.save(refreshToken);
        }
    }

}

