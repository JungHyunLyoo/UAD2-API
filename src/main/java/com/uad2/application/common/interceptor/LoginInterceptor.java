package com.uad2.application.common.interceptor;

/*
 * @USER Jongyeob Kim
 * @DATE 2019-10-05
 * @DESCRIPTION 로그인 인터셉터. 자동 로그인/일반 로그인을 처리한다.
 */

import com.uad2.application.common.enumData.CookieName;
import com.uad2.application.member.LoginProcessor;
import com.uad2.application.member.entity.Member;
import com.uad2.application.member.service.MemberService;
import com.uad2.application.utils.CookieUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

@Component
public class LoginInterceptor extends HandlerInterceptorAdapter {

    private Logger logger = LoggerFactory.getLogger(LoginInterceptor.class);

    @Autowired
    private LoginProcessor loginProcessor;

    @Autowired
    private MemberService memberService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        logger.info("-----Login interceptor-----");
        //쿠키 내 로그인 데이터 없을 경우, 로그인 페이지로 이동
        if (loginProcessor.isEmptyLoginCookie(request)) {
            //redirect url은 나중에 확정
            response.sendRedirect(request.getContextPath() + "/");
            return false;
        }
        else{
            //쿠키 내 로그인 데이터 get
            List<Cookie> cookieList = Arrays.asList(request.getCookies());
            Cookie sessionIdInCookie = CookieUtil.getCookie(cookieList, CookieName.SESSION_ID);
            Cookie idInCookie = CookieUtil.getCookie(cookieList, CookieName.ID);
            //쿠키 내 로그인 데이터로 db 내 로그인 데이터 일치 확인
            //일치하지 않을 경우, 로그인 페이지로 이동
            Member member = memberService.getMemberByIdAndSessionId(idInCookie.getValue(),sessionIdInCookie.getValue());
            if (member != null) {
                loginProcessor.autoLogin(request.getSession(),response,member);
                return true;
            }
            else{
                response.sendRedirect(request.getContextPath() + "/");
                return false;

            }
        }
    }

}
