package com.uad2.application.member;

/*
 * @USER JongyeobKim
 * @DATE 2019-10-08
 * @DESCRIPTION 세션 유효성 체크
 */

import com.uad2.application.member.entity.Member;

import java.util.Calendar;
import java.util.TimeZone;

public class SessionValidator {

    public static boolean isSessionExpired(Member member) {
        // 저장되어 있는 세션의 만료시점이 현재 시간보다 이전인 경우, 유효하지 않은 세션으로 간주한다.
        return member.getSessionLimit().before(Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTime());
    }
}
