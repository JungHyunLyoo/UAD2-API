package com.uad2.application.member.resource;
/*
 * @USER JungHyun
 * @DATE 2019-09-12
 */

import com.uad2.application.member.entity.MemberExternalDto;
import org.springframework.hateoas.ResourceSupport;

import java.util.List;

public class MemberListExternalResource extends ResourceSupport {//.add로 링크추가 가능
    private List<MemberExternalDto> memberExternalDtoList;

    public MemberListExternalResource(List<MemberExternalDto> memberExternalDtoList){
        this.memberExternalDtoList = memberExternalDtoList;
    }

    public List<MemberExternalDto> getMemberList() {
        return memberExternalDtoList;
    }
}
