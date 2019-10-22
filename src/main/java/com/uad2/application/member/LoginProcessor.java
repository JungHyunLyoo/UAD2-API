package com.uad2.application.member;

/*
 * @USER Jongyeob Kim
 * @DATE 2019-10-05
 * @DESCRIPTION 로그인 처리기 (자동 로그인, 일반 로그인)
 */

import com.uad2.application.common.enumData.CookieName;
import com.uad2.application.member.dto.MemberDto;
import com.uad2.application.member.entity.Member;
import com.uad2.application.member.service.MemberService;
import com.uad2.application.utils.CookieUtil;
import com.uad2.application.utils.EncryptUtil;
import com.uad2.application.utils.SessionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.Calendar;
import java.util.TimeZone;

@Component
public class LoginProcessor {

    public static final int INVALID_PWD = 0;
    public static final int LOGIN_SUCCESS = 1;

    @Autowired
    private MemberService memberService;

    /**
     * 일반 로그인 처리 메소드
     */
    public int login(HttpSession session, HttpServletResponse response, MemberDto.LoginRequest loginRequest) {
        //id,pwd 검증
        Member member = memberService.getMemberById(loginRequest.getId());
        boolean isValidPwd = member.getPwd().equals(EncryptUtil.encryptMD5(loginRequest.getPwd()));
        if (!isValidPwd) {
            return INVALID_PWD;
        }
        //세션 저장 및 자동 로그인 처리
        SessionUtil.setSession(session, "member", member);
        if(loginRequest.isAutoLogin()){
            String sessionId = session.getId();
            this.setLoginInDB(member,sessionId);
            this.setLoginCookie(response, member, sessionId);
        }
        return LOGIN_SUCCESS;
    }

    /**
     * 자동 로그인 처리 메소드
     */
    public void autoLogin(HttpSession session, HttpServletResponse response,Member member) {
        //세션 저장 및 자동 로그인 처리
        SessionUtil.setSession(session, "member", member);
        String sessionId = session.getId();
        this.setLoginInDB(member,sessionId);
        this.setLoginCookie(response, member, sessionId);
    }

    private void setLoginInDB(Member member, String sessionId) {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.add(Calendar.YEAR, 1);
        memberService.updateSessionInfo(member, sessionId, calendar.getTime());
    }

    private void setLoginCookie(HttpServletResponse response, Member member, String sessionId) {
        response.addCookie(CookieUtil.setCookie(CookieName.ID, member.getId(),CookieUtil.A_YEAR_EXPIRATION));
        response.addCookie(CookieUtil.setCookie(CookieName.NAME, member.getName(),CookieUtil.A_YEAR_EXPIRATION));
        response.addCookie(CookieUtil.setCookie(CookieName.PHONE_NUM, member.getPhoneNumber(),CookieUtil.A_YEAR_EXPIRATION));
        response.addCookie(CookieUtil.setCookie(CookieName.IS_WORKER, Integer.toString(member.getIsWorker()),CookieUtil.A_YEAR_EXPIRATION));
        response.addCookie(CookieUtil.setCookie(CookieName.SESSION_ID, sessionId,CookieUtil.A_YEAR_EXPIRATION));
        response.addCookie(CookieUtil.setCookie(CookieName.IS_ADMIN, Integer.toString(member.getIsAdmin()),CookieUtil.A_YEAR_EXPIRATION));
    }

    public boolean isEmptyLoginCookie(HttpServletRequest request){
        Cookie[] cookieArray = request.getCookies();
        return !(CookieUtil.getCookie(Arrays.asList(cookieArray),CookieName.ID) != null &&
                 CookieUtil.getCookie(Arrays.asList(cookieArray),CookieName.NAME) != null &&
                 CookieUtil.getCookie(Arrays.asList(cookieArray),CookieName.PHONE_NUM) != null &&
                 CookieUtil.getCookie(Arrays.asList(cookieArray),CookieName.IS_WORKER) != null &&
                 CookieUtil.getCookie(Arrays.asList(cookieArray),CookieName.SESSION_ID) != null &&
                 CookieUtil.getCookie(Arrays.asList(cookieArray),CookieName.IS_ADMIN) != null);
    }
}
