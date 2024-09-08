package com.tosi.user.aspect;

import com.ssafy.tosi.cookieUtil.CookieUtil;
import com.ssafy.tosi.jwt.JwtUtil;
import com.ssafy.tosi.jwt.TokenController;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class JwtAspect {
    private final JwtUtil jwtUtil;
    private final CookieUtil cookieUtil;

    private final TokenController tokenController;

    @Around("execution(* com.ssafy.tosi.*..*Controller.*(..)) && !execution(* com.ssafy.tosi.user.UserController.postUser(..)) " +
            "&& !execution(* com.ssafy.tosi.user.UserController.post*Login(..)) " +
            "&& !execution(* com.ssafy.tosi.user.UserController.get*Logout(..)) " +
            "&& !execution(* com.ssafy.tosi.user.UserController.getEmailCheck(..)) " +
            "&& !execution(* com.ssafy.tosi.jwt.TokenController.postNewAccessToken(..)) && args(request, ..)")
    public Object aroundServiceMethod2(ProceedingJoinPoint joinPoint, HttpServletRequest request) throws Throwable {

        String accessToken = cookieUtil.getTokenFromCookie(request, "access-token");

        if (accessToken == null) {
            System.out.println("토큰이 존재하지 않습니다.");
            return tokenController.postNewAccessToken(request);
        }

        boolean tokenValidity = jwtUtil.validateToken(accessToken);

        if (!tokenValidity) {
            System.out.println("토큰이 만료되었습니다.");
            return tokenController.postNewAccessToken(request);
        }

        Integer userId = jwtUtil.getUserId(accessToken);

        request.setAttribute("userId", userId);

        return joinPoint.proceed();

    }

}
