package com.uad2.application.utils;

/*
 * @USER Jongyeob Kim
 * @DATE 2019-10-08
 */

import javax.servlet.http.HttpSession;

public class SessionUtil {

    /**
     * 세션 설정
     * 만료 365일
     */
    public static void setSession(HttpSession session, String name, Object object) {
        final int expiredTime = 60 * 60 * 24;
        session.setAttribute(name, object);
        session.setMaxInactiveInterval(expiredTime * 365);
    }

}
