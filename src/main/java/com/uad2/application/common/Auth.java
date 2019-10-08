package com.uad2.application.common;

/*
 * @USER Jongyeob Kim
 * @DATE 2019-10-08
 * @DESCRIPTION 인증 어노테이션 추가 (인터셉터 처리를 위해 우선 적용. 추후 AOP로 변경 할 수도 있음)
 */

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Auth {
    Role role() default Role.USER;
}
