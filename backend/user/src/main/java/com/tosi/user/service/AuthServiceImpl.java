package com.tosi.user.service;

import com.tosi.user.common.JWT.JwtExpiration;
import com.tosi.user.common.JWT.JwtTokenUtil;
import com.tosi.user.common.JWT.TokenInfo;
import com.tosi.user.common.exception.CustomException;
import com.tosi.user.common.exception.ExceptionCode;
import com.tosi.user.common.exception.SuccessResponse;
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
        // 회원 번호로 엑세스 토큰 생성
        String userId = user.getUserId().toString();
        String accessToken = jwtTokenUtil.generateAccessToken(userId);
        // 회원 번호로 리프레시 토큰 생성
        RefreshToken refreshToken = saveRefreshToken(userId);

        return TokenInfo.of(accessToken, refreshToken.getRefreshToken());
    }

    /**
     * JWT 토큰에서 회원 번호을 추출하여 반환합니다.
     *
     * @param accessToken 인증된 사용자의 JWT 토큰
     * @return 토큰에서 추출한 사용자의 이메일
     * @throws CustomException 토큰이 유효하지 않거나 회원 번호를 찾을 수 없음
     */
    @Override
    public String findUserAuthorization(String accessToken) {
        String userId = jwtTokenUtil.getUsername(accessToken.substring(7));
        if (userId == null)
            throw new CustomException(ExceptionCode.INVALID_TOKEN);

        return userId;
    }

    /**
     * 로그아웃을 요청한 회원의 토큰을 무효화 시킵니다.
     * 토큰의 유효기간이 끝날 때까지 Redis에 보관하여 재사용되지 않도록 합니다.
     *
     * @param tokenInfo 로그아웃 처리할 회원의 Access Token과 Refresh Token 정보
     * @param userId 로그아웃 처리할 회원 번호(Redis 캐싱 키)
     * @return 로그아웃 처리가 완료되면 SuccessResponse 반환
     */
    @Override
    public SuccessResponse invalidateToken(TokenInfo tokenInfo, String userId) {
        // 순수한 엑세스 토큰 추출
        String accessToken = resolveToken(tokenInfo.getAccessToken());
        // 토큰의 남은 유효기간 계산
        long remainMilliSeconds = jwtTokenUtil.getRemainMilliSeconds(accessToken);
        // 레디스에서 리프레시토큰 삭제
        refreshTokenRedisRepository.deleteById(userId);
        // 토큰이 남은 유효기간 동안 사용되지 않도록 블랙리스트에 저장
        logoutAccessTokenRedisRepository.save(LogoutAccessToken.of(accessToken, userId, remainMilliSeconds));

        return SuccessResponse.of("로그아웃 되었습니다.");
    }

    /**
     * 리프레시 토큰을 검증하고, 유효하면 새로운 액세스 토큰을 발급합니다.
     *
     * @param refreshToken 로그인한 회원의 리프레시 토큰
     * @return 새로운 액세스 토큰과 기존 리프레시 토큰이 포함된 TokenInfo 객체
     */
    public TokenInfo reissue(String refreshToken) {
        // 현재 로그인한 회원의 번호를 기반으로 레디스에서 저장된 리프레시 토큰 조회
        String userId = getCurrentUsername();
        RefreshToken redisRefreshToken = refreshTokenRedisRepository.findById(userId)
                .orElseThrow(NoSuchElementException::new);

        // 리프레시 토큰이 레디스에 저장된 토큰과 일치하면 새로운 액세스 토큰을 발급
        if (!refreshToken.equals(redisRefreshToken.getRefreshToken()))
            throw new CustomException(ExceptionCode.INVALID_TOKEN);

        return reissueRefreshToken(refreshToken, userId);
    }

    private String resolveToken(String token) {
        return token.substring(7);
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
            throw new CustomException(ExceptionCode.INVALID_CREDENTIALS);
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
