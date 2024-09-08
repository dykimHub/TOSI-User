package com.tosi.user.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Date;


//@RequiredArgsConstructor
@Component
public class JwtUtil {

    @Autowired
    private JwtProperties jwtProperties;

    public String generateToken(Integer userId, Duration expiredAt) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expiredAt.toMillis());

        return Jwts.builder()
                .setHeaderParam("algo", "HS256")
                .setHeaderParam("type", "JWT")
                .setIssuer(jwtProperties.getIssuer())
                .setIssuedAt(now)
                .setExpiration(expiry)
//                .setSubject(userId)
                .claim("userId", userId)
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecretKey())
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(jwtProperties.getSecretKey())
                    .parseClaimsJws(token);

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // 클레임 중 userId 가져오기
    public Integer getUserId(String token) {
        Claims claims = getClaims(token);
        return claims.get("userId", Integer.class);
    }

    // 토큰에서 클레임(토큰에 담긴 정보) 가져오기
    private Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(jwtProperties.getSecretKey())
                .parseClaimsJws(token)
                .getBody();
    }
}
