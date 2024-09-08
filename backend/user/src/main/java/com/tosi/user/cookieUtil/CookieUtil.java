package com.tosi.user.cookieUtil;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

//@NoArgsConstructor
//@RequiredArgsConstructor
@Component
public class CookieUtil {

    public static void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setMaxAge(maxAge);

        response.addCookie(cookie);
    }

    public static void deleteCookie(HttpServletRequest request, HttpServletResponse response, String name) {
        Cookie[] cookies = request.getCookies();

        if (cookies == null) {
            return;
        }

        for (Cookie cookie : cookies) {
            if (name.equals(cookie.getName())) {
                cookie.setValue("");
                cookie.setPath("/");
                cookie.setMaxAge(0);
                response.addCookie(cookie);
            }
        }
    }

    public static String getTokenFromCookie(HttpServletRequest request, String token) {
        String foundToken = "";

        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            String cookieName = token;

            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(cookieName)) {
                    foundToken = cookie.getValue();
                    return foundToken;
                }
            }
        }
        return null;
    }

}