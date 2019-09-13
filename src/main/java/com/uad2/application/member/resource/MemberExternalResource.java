package com.uad2.application.member.resource;
/*
 * @USER JungHyun
 * @DATE 2019-09-12
 * @DESCRIPTION api 반환용 회원 HAL 포멧
 */

import com.uad2.application.member.entity.MemberExternalDto;
import org.springframework.hateoas.ResourceSupport;

public class MemberExternalResource extends ResourceSupport {//.add로 링크추가 가능
    private MemberExternalDto memberExternalDto;

    public MemberExternalResource(MemberExternalDto memberExternalDto){
        this.memberExternalDto = memberExternalDto;
    }

    public MemberExternalDto getMember() {
        return memberExternalDto;
    }//return되는 필드명
}
