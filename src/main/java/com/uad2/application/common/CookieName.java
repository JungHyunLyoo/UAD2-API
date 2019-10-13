package com.uad2.application.common;

/*
 * @USER Jongyeob Kim
 * @DATE 2019-10-09
 * @DESCRIPTION 공용 쿠키의 name을 정의한 enum 객체
 */

import lombok.Getter;

@Getter
public enum CookieName {

    ID("id"),
    NAME("name"),
    PHONE_NUM("phone_num"),
    IS_WORKER("is_worker"),
    SESSION_ID("session_id"),
    IS_ADMIN("is_admin"),
    IS_AUTO_LOGIN("is_auto_login");

    private String name;

    CookieName(String name) {
        this.name = name;
    }

}
