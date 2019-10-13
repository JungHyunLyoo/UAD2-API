package com.uad2.application.common;

/*
 * @USER Jongyeob Kim
 * @DATE 2019-10-05
 * @DESCRIPTION 로그인 인터셉터. 자동 로그인/일반 로그인을 처리한다.
 */

import com.uad2.application.exception.ClientException;
import com.uad2.application.member.LoginProcessor;
import com.uad2.application.utils.CookieUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.Objects;

@Component
public class LoginInterceptor extends HandlerInterceptorAdapter {

    private Logger logger = LoggerFactory.getLogger(LoginInterceptor.class);

    @Autowired
    private LoginProcessor loginProcessor;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        logger.info("-----Login interceptor-----");
        HttpSession session = request.getSession();

        // 쿠키 정보 담겨있는 경우 자동 로그인 로직을 탄다.
        if (Objects.isNull(request.getCookies())) {
            return true;
        }

        Cookie sessionIdInCookie = CookieUtil.getCookie(Arrays.asList(request.getCookies()), CookieName.SESSION_ID);
        Cookie idInCookie = CookieUtil.getCookie(Arrays.asList(request.getCookies()), CookieName.ID);
        Cookie isAutoLoginInCookie = CookieUtil.getCookie(Arrays.asList(request.getCookies()), CookieName.IS_AUTO_LOGIN);

        // 자동 로그인 처리를 위한 null 체크
        if (Objects.nonNull(sessionIdInCookie) && Objects.nonNull(idInCookie) && Objects.nonNull(isAutoLoginInCookie)) {
            // 세션 id와 쿠키에 담긴 session_id가 동일하면 이미 로그인 된 경우이다. (세션이 살아있다)
            if (session.getId().equals(sessionIdInCookie.getValue())) {
                throw new ClientException("Already logged in");
            }

            // 쿠키에 담긴 session_id가 빈 String이 아니면 자동 로그인 로직을 탄다.
            if (!sessionIdInCookie.getValue().equals("")) {
                loginProcessor.login(session, response, sessionIdInCookie.getValue(), idInCookie.getValue(), Boolean.valueOf(isAutoLoginInCookie.getValue()));
                return false;   // 다음 수행을 컨트롤러로 넘기지 않고 반환한다.
            }
        }

        return true; // 다음 수행을 컨트롤러로 넘긴다.
    }

}
