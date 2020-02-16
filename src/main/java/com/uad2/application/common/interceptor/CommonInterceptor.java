package com.uad2.application.common.interceptor;

/*
 * @USER Jongyeob Kim
 * @DATE 2019-10-05
 * @DESCRIPTION 컨트롤러에 대한 인터셉터. 로그인 세션 정보를 확인한다.
 */

import com.uad2.application.common.annotation.Auth;
import com.uad2.application.common.enumData.CookieName;
import com.uad2.application.common.enumData.Role;
import com.uad2.application.exception.ClientException;
import com.uad2.application.exception.ForbiddenException;
import com.uad2.application.member.LoginProcessor;
import com.uad2.application.member.dto.MemberDto;
import com.uad2.application.member.entity.Member;
import com.uad2.application.member.service.MemberService;
import com.uad2.application.utils.CookieUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Component
public class CommonInterceptor extends HandlerInterceptorAdapter {

    private Logger logger = LoggerFactory.getLogger(CommonInterceptor.class);

    private final LoginProcessor loginProcessor;
    private final MemberService memberService;

    @Autowired
    public CommonInterceptor(LoginProcessor loginProcessor,
                             MemberService memberService){
        this. loginProcessor = loginProcessor;
        this. memberService = memberService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        logger.info("-----Common interceptor start-----");
        // handler : 인터셉터 이후에 실행할 컨트롤러의 메소드
        if (!(handler instanceof HandlerMethod)) {
            // 컨트롤러에 존재하지 않는 메소드인 경우 그대로 컨트롤러로 넘긴다.
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Auth auth = handlerMethod.getMethodAnnotation(Auth.class);
        if (Objects.isNull(auth)) {
            // @Auth 어노테이션 없는 메소드인 경우(public 엔드포인트) 컨트롤러로 바로 넘긴다.
            return true;
        }

        HttpSession session = request.getSession();

        //쿠키 획득
        List<Cookie> cookieList = Optional.ofNullable(request.getCookies())
                                    .map(Arrays::asList)
                                    .orElseThrow(() -> new ClientException("Cookies are not exist"));

        //자동로그인 체크
        loginProcessor.checkAutoLogin(cookieList);

        //계정 존재 여부 체크
        Member member = Optional.ofNullable(memberService.getMemberById(CookieUtil.getCookie(cookieList, CookieName.ID).getValue()))
                .orElseThrow(() -> new ClientException("Id is not exist"));

        //계정 열람 권한 체크
        if(auth.role() == Role.ADMIN && member.getIsAdmin() == 0){
            throw new ForbiddenException("Member auth is not valid");
        }

        //로그인 실행
        MemberDto.LoginRequest loginRequest = MemberDto.LoginRequest.builder()
                                                .id(member.getId())
                                                .pwd(member.getPwd())
                                                .isAutoLogin(Objects.nonNull(member.getSessionId()))
                                                .build();
        loginProcessor.login(request,response,session,loginRequest);
        return true;
    }
}
