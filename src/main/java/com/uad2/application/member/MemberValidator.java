package com.uad2.application.member;
/*
 * @USER JungHyun
 * @DATE 2019-09-09
 */

import com.uad2.application.exception.ClientException;
import com.uad2.application.member.dto.MemberDto;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

@Component
public class MemberValidator {
    public void validateCreateMember(MemberDto.Request requestMember) {
        for (Field declaredField : requestMember.getClass().getDeclaredFields()) {
            //private 필드에 접근 허용
            declaredField.setAccessible(true);
            try {
                Object value = declaredField.get(requestMember);
                if(!declaredField.getName().equals("isAutoLogin") && (value == null || value.toString().isEmpty())){
                    throw new ClientException(declaredField.getName() + " is empty");
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException("validateCreateMember error");
            }
        }
    }
}
