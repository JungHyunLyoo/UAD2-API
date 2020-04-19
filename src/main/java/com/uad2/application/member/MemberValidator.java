package com.uad2.application.member;
/*
 * @USER JungHyun
 * @DATE 2019-09-09
 */

import com.uad2.application.exception.ClientException;
import com.uad2.application.member.dto.MemberDto;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.util.Objects;

@Component
public class MemberValidator {
    public void validateCreateMember(MemberDto.Request requestMember) {
        for (Field declaredField : requestMember.getClass().getDeclaredFields()) {
            //private 필드에 접근 허용
            declaredField.setAccessible(true);
            try {
                if (StringUtils.isEmpty(declaredField.get(requestMember))) {
                    throw new ClientException(declaredField.getName() + " is empty");
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException("validateCreateMember error");
            }
        }
    }

    public void validateCheckPwd(MemberDto.Request requestMember) {
        if (Objects.isNull(requestMember.getId())) {
            throw new ClientException("Id is empty");
        } else if (Objects.isNull(requestMember.getPwd())) {
            throw new ClientException("Pwd is empty");
        }
    }
}
