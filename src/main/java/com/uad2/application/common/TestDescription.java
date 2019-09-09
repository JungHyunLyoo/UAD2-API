package com.uad2.application.common;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/*
 * @USER JungHyun
 * @DATE 2019-09-09
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.SOURCE)//유지 영역
public @interface TestDescription {
    String value();
}
