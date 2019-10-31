package com.uad2.application.utils;

/*
 * @USER Jongyeob Kim
 * @DATE 2019-10-08
 */

import javax.servlet.http.HttpSession;

public class SessionUtil {

    public static final int A_DAY_EXPIRATION = 60 * 60 * 24;

    public static void setAttribute(HttpSession session, String name, Object object) {
        setAttribute(session,name,object,A_DAY_EXPIRATION);
    }

    /**
     * 세션 추가
     */
    public static void setAttribute(HttpSession session, String name, Object object,int expiredTime) {
        session.setAttribute(name, object);
        session.setMaxInactiveInterval(expiredTime);
    }

    public static Object getAttribute(HttpSession session, String name){
        return session.getAttribute(name);
    }

    public static void removeAttribute(HttpSession session, String name){
        session.removeAttribute(name);
    }
}
