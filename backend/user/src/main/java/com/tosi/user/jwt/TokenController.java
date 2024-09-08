package com.tosi.user.jwt;

import com.ssafy.tosi.cookieUtil.CookieUtil;
import com.ssafy.tosi.jwt.service.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RequiredArgsConstructor
@RequestMapping("/jwt")
@RestController
//@CrossOrigin("*")
public class TokenController {

    private final TokenService tokenService;
    private final CookieUtil cookieUtil;
    private final JwtUtil jwtUtil;

    @PostMapping("/new-access-token")
    public ResponseEntity<?> postNewAccessToken(HttpServletRequest request) {

        String refreshToken = cookieUtil.getTokenFromCookie(request, "refresh-token");
        System.out.println("TokenController_리프레쉬토큰:" + refreshToken);

        Map<String, String> map = tokenService.generateNewAccessToken(refreshToken);

        if(map == null) {
            return new ResponseEntity<Void>(HttpStatus.FOUND);
        }

        return new ResponseEntity<Map>(map, HttpStatus.CREATED);
    }

}
