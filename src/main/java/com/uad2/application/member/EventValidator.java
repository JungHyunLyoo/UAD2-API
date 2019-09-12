package com.uad2.application.member;
/*
 * @USER JungHyun
 * @DATE 2019-09-09
 */

import com.uad2.application.member.entity.MemberExternalDto;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
public class EventValidator {
    public void validate(MemberExternalDto memberExternalDto, Errors errors){
        System.out.println("EventValidator  :: validate");
        if("testidtest".equals(memberExternalDto.getId())){
            errors.rejectValue("id","wo","id is wrong");
        }
    }

}
