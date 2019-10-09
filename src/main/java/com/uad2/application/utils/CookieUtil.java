package com.uad2.application.utils;

/*
 * @USER Jongyeob Kim
 * @DATE 2019-10-08
 * @DESCRIPTION 공용 쿠키 유틸리티
 */

import com.uad2.application.common.CookieName;

import javax.servlet.http.Cookie;
import java.util.List;
import java.util.stream.Collectors;

public class CookieUtil {

    /**
     * 쿠키 리스트로부터 원하는 쿠키만 반환한다.
     */
    public static Cookie getCookie(List<Cookie> cookies, CookieName cookieName) {
        return cookies.stream()
                .filter(c -> c.getName().equals(cookieName.getName()))
                .findFirst()
                .orElse(null);
    }

    /**
     * 쿠키 설정
     * 만료 30일. /api 하위 URI에서 유효. HTTP 프로토콜에서만 가능
     */
    public static Cookie setCookie(CookieName cookieName, String value) {
        final int expiredTime = 60 * 60 * 24;
        Cookie cookie = new Cookie(cookieName.getName(), value);
        cookie.setMaxAge(expiredTime * 30);
        cookie.setPath("/api");
        cookie.setHttpOnly(true);

        return cookie;
    }

    /**
     * 쿠키 삭제
     */
    public static List<Cookie> removeAllCookies(List<Cookie> cookies) {
        return cookies.stream()
                .peek(cookie -> {
                    cookie.setValue(null);
                    cookie.setMaxAge(0);
                    cookie.setPath("/api");
                    cookie.setHttpOnly(true);
                })
                .collect(Collectors.toList());
    }

}
