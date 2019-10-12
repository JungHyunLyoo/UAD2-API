package com.uad2.application.utils;

/*
 * @USER Jongyeob Kim
 * @DATE 2019-10-08
 */

import javax.servlet.http.HttpSession;

public class SessionUtil {

    private static final int A_DAY_EXPIRATION = 60 * 60 * 24;

    /**
     * 세션 설정
     */
    public static void setSession(HttpSession session, String name, Object object) {
        setSession(session,name,object,A_DAY_EXPIRATION);
    }

    /**
     * 세션 설정
     */
    public static void setSession(HttpSession session, String name, Object object,int expiredTime) {
        session.setAttribute(name, object);
        session.setMaxInactiveInterval(expiredTime);
    }
}
