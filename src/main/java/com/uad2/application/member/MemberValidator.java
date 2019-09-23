package com.uad2.application.member;
/*
 * @USER JungHyun
 * @DATE 2019-09-09
 */

import com.uad2.application.member.dto.MemberDto;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
public class MemberValidator {
    public void createMemberValidate(MemberDto.Request requestMember, Errors errors){
        String id = requestMember.getId();
        if(id == null || "".equals(id)){
            
            errors.rejectValue("id","wrongValue", "id is empty");
        }
    }
}
