package com.uad2.application.member;
/*
 * @USER JungHyun
 * @DATE 2019-09-09
 */

import com.uad2.application.exception.ClientException;
import com.uad2.application.member.dto.MemberDto;
import org.springframework.stereotype.Component;

@Component
public class MemberValidator {
    public void validateCreateMember(MemberDto.Request requestMember) throws ClientException {
        String id = requestMember.getId();
        if(id == null || id.isEmpty()){
            throw new ClientException("id fail");
        }
    }
}
