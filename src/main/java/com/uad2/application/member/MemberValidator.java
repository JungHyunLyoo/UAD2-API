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
    public void createMemberValidate(MemberDto.Request request, Errors errors){
        String id = request.getId();
        if(id == null || "".equals(id)){
            
            errors.rejectValue("id","wrongValue", "id is empty");
        }
    }
}
