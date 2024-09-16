package com.tosi.user.service;

import com.tosi.user.common.JWT.JwtExpiration;
import com.tosi.user.common.JWT.JwtTokenUtil;
import com.tosi.user.common.JWT.TokenInfo;
import com.tosi.user.common.exception.CustomException;
import com.tosi.user.common.exception.ExceptionCode;
import com.tosi.user.common.redis.entity.LogoutAccessToken;
import com.tosi.user.common.redis.entity.RefreshToken;
import com.tosi.user.common.redis.repository.LogoutAccessTokenRedisRepository;
import com.tosi.user.common.redis.repository.RefreshTokenRedisRepository;
import com.tosi.user.dto.LoginDto;
import com.tosi.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

import static com.tosi.user.common.JWT.JwtExpiration.REFRESH_TOKEN_EXPIRATION_TIME;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {
    private final JwtTokenUtil jwtTokenUtil;
    private final RefreshTokenRedisRepository refreshTokenRedisRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final LogoutAccessTokenRedisRepository logoutAccessTokenRedisRepository;

    /**
     * 로그인 객체의 비밀번호와 데이터베이스에서 조회된 회원의 비밀번호가 일치하는지 확인합니다.
     * 일치할 경우, 엑세스 토큰과 리프레시 토큰을 생성하여 JWT 토큰 정보를 반환합니다.
     *
     * @param loginDto 로그인 정보가 담긴 loginDto 객체
     * @param user     데이터베이스에서 찾은 로그인한 회원 정보
     * @return TokenInfo 객체
     */
    @Override
    public TokenInfo findTokenInfo(LoginDto loginDto, User user) {
        // 비밀번호 일치 검증
        checkPassword(loginDto.getPassword(), user.getPassword());
        // 엑세스 토큰 생성
        String accessToken = jwtTokenUtil.generateAccessToken(user.getEmail());
        // 리프레시 토큰 생성
        RefreshToken refreshToken = saveRefreshToken(user.getEmail());

        return TokenInfo.of(accessToken, refreshToken.getRefreshToken());
    }

    /**
     * JWT 토큰에서 이메일을 추출하여 반환합니다.
     *
     * @param accessToken 인증된 사용자의 JWT 토큰
     * @return 토큰에서 추출한 사용자의 이메일
     * @throws CustomException 토큰이 유효하지 않거나 이메일을 찾을 수 없음
     */
    @Override
    public String findUserAuthorization(String accessToken) {
        String email = jwtTokenUtil.getUsername(accessToken.substring(7));
        if (email == null)
            throw new CustomException(ExceptionCode.INVALID_TOKEN);

        return email;
    }

    /**
     * 로그아웃된 토큰을 유효기간이 끝날 때까지 Redis에 보관하여 재사용되지 않도록 합니다.
     *
     * @param tokenInfo 로그아웃 처리할 회원의 Access Token과 Refresh Token 정보
     * @param email 로그아웃 처리할 회원의 이메일(Redis 캐싱 키)
     */
    @Override
    public void invalidateToken(TokenInfo tokenInfo, String email) {
        // 순수한 토큰 추출
        String accessToken = resolveToken(tokenInfo.getAccessToken());
        // 토큰의 남은 유효기간 계산
        long remainMilliSeconds = jwtTokenUtil.getRemainMilliSeconds(accessToken);
        // 레디스에서 리프레시토큰 삭제
        refreshTokenRedisRepository.deleteById(email);
        // 토큰이 남은 유효기간 동안 사용되지 않도록 블랙리스트에 저장
        logoutAccessTokenRedisRepository.save(LogoutAccessToken.of(accessToken, email, remainMilliSeconds));
    }

    private String resolveToken(String token) {
        return token.substring(7);
    }

    private TokenInfo reissue(String refreshToken) {
        refreshToken = resolveToken(refreshToken);
        String username = getCurrentUsername();
        RefreshToken redisRefreshToken = refreshTokenRedisRepository.findById(username).orElseThrow(NoSuchElementException::new);

        if (refreshToken.equals(redisRefreshToken.getRefreshToken())) {
            return reissueRefreshToken(refreshToken, username);
        }
        throw new IllegalArgumentException("토큰이 일치하지 않습니다.");
    }

    private TokenInfo reissueRefreshToken(String refreshToken, String username) {
        if (lessThanReissueExpirationTimesLeft(refreshToken)) {
            return TokenInfo.of(jwtTokenUtil.generateAccessToken(username), saveRefreshToken(username).getRefreshToken());
        }
        return TokenInfo.of(jwtTokenUtil.generateAccessToken(username), refreshToken);
    }

    private boolean lessThanReissueExpirationTimesLeft(String refreshToken) {
        return jwtTokenUtil.getRemainMilliSeconds(refreshToken) < JwtExpiration.REISSUE_EXPIRATION_TIME.getValue();
    }

    private String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails principal = (UserDetails) authentication.getPrincipal();
        return principal.getUsername();
    }

    private void checkPassword(String rawPassword, String userPassword) {
        if (!passwordEncoder.matches(rawPassword, userPassword)) {
            throw new BadCredentialsException("아이디 또는 비밀번호가 잘못되었습니다.");
        }
    }

    private RefreshToken saveRefreshToken(String userIdToString) {
        return refreshTokenRedisRepository.save(
                RefreshToken.createRefreshToken(
                        userIdToString,
                        jwtTokenUtil.generateRefreshToken(userIdToString), REFRESH_TOKEN_EXPIRATION_TIME.getValue())
        );
    }
}
