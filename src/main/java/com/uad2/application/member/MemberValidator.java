package com.uad2.application.member;
/*
 * @USER JungHyun
 * @DATE 2019-09-09
 */

import com.uad2.application.member.entity.MemberInsertDto;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
public class MemberValidator {
    public void createMemberValidate(MemberInsertDto memberInsertDto, Errors errors){
        String id = memberInsertDto.getId();
        if(id == null || "".equals(id)){
            errors.rejectValue("id","wrongValue", "id is empty");
            System.out.println("getFieldErrors()");
            System.out.println(errors.getFieldErrors());
        }
    }
    public void checkMemberByIdValidate(String id,Errors errors){
        if(id == null || "".equals(id)){
            errors.rejectValue("id","wrongValue", "id is empty");
            System.out.println("getFieldErrors()");
            System.out.println(errors.getFieldErrors());
        }
    }
}
