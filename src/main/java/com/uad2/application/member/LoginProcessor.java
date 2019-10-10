package com.uad2.application.member;

/*
 * @USER Jongyeob Kim
 * @DATE 2019-10-05
 * @DESCRIPTION 로그인 처리기 (자동 로그인, 일반 로그인)
 */

import com.uad2.application.common.CookieName;
import com.uad2.application.exception.ClientException;
import com.uad2.application.member.dto.MemberDto;
import com.uad2.application.member.entity.Member;
import com.uad2.application.member.service.MemberService;
import com.uad2.application.utils.CookieUtil;
import com.uad2.application.utils.EncryptUtil;
import com.uad2.application.utils.SessionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Calendar;
import java.util.Optional;
import java.util.TimeZone;

import static com.uad2.application.member.SessionValidator.isSessionExpired;

@Component
public class LoginProcessor {

    @Autowired
    private MemberService memberService;

    /**
     * 일반 로그인 처리 메소드
     */
    public void login(HttpSession session, HttpServletResponse response, MemberDto.LoginRequest requestLogin) {
        Member member = this.isAccountValid(requestLogin.getId(), requestLogin.getPwd());
        this.updateSessionInfo(session, member);
        this.updateCookieInfo(response, member, session.getId(), requestLogin.isAutoLogin());
    }

    /**
     * 자동 로그인 처리 메소드
     */
    public void login(HttpSession session, HttpServletResponse response, String sessionIdInCookie, String id, boolean isAutoLogin) {
        Member member = this.isSessionValid(sessionIdInCookie, id);
        this.updateSessionInfo(session, member);
        this.updateCookieInfo(response, member, session.getId(), isAutoLogin);
    }

    /**
     * 입력된 로그인 정보와 실제 계정 정보 유효한지 확인한다.
     */
    private Member isAccountValid(String id, String pwd) {
        Member member = Optional.ofNullable(memberService.getMemberById(id))
                .orElseThrow(() -> new ClientException("Member is not exist"));

        if (!member.getPwd().equals(EncryptUtil.encryptMD5(pwd))) {
            throw new ClientException("Password is not matched");
        }

        return member;
    }

    /**
     * 쿠키에 담긴 session_id로 해당 회원의 세션이 유효한지 확인한다.
     */
    private Member isSessionValid(String sessionId, String id) {
        Member member = Optional.ofNullable(memberService.getMemberByIdAndSessionId(id, sessionId))
                .orElseThrow(() -> new ClientException("Member is not exist"));

        if (isSessionExpired(member)) {
            throw new ClientException("Session has expired");
        }

        return member;
    }

    /**
     * 세션 정보를 업데이트 한다..
     */
    public void updateSessionInfo(HttpSession session, Member member) {
        // 세션 설정
        SessionUtil.setSession(session, "member", member);

        // 회원 DB의 세션 정보 업데이트
        this.updateSessionInfoInRepository(member, session.getId(), session.getMaxInactiveInterval());
    }

    /**
     * 자동 로그인 true인 경우 쿠키 정보를 추가한다.
     */
    private void updateCookieInfo(HttpServletResponse response, Member member, String sessionId, boolean isAutoLogin) {
        if (isAutoLogin) {
            // 쿠키 정보 추가
            response.addCookie(CookieUtil.setCookie(CookieName.ID, member.getId()));
            response.addCookie(CookieUtil.setCookie(CookieName.NAME, member.getName()));
            response.addCookie(CookieUtil.setCookie(CookieName.PHONE_NUM, member.getPhoneNumber()));
            response.addCookie(CookieUtil.setCookie(CookieName.IS_WORKER, Integer.toString(member.getIsWorker())));
            response.addCookie(CookieUtil.setCookie(CookieName.SESSION_ID, sessionId));
            response.addCookie(CookieUtil.setCookie(CookieName.IS_ADMIN, Integer.toString(member.getIsAdmin())));
            response.addCookie(CookieUtil.setCookie(CookieName.IS_AUTO_LOGIN, Boolean.toString(true)));
        }
    }

    /**
     * 회원의 DB 세션 정보를 업데이트 한다.
     */
    private void updateSessionInfoInRepository(Member member, String sessionId, int expiredTime) {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.add(Calendar.SECOND, expiredTime);
        memberService.updateSessionInfo(member, sessionId, calendar.getTime());
    }

}
