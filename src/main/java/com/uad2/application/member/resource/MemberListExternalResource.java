package com.uad2.application.member.resource;
/*
 * @USER JungHyun
 * @DATE 2019-09-12
 * @DESCRIPTION api 반환용 회원 리스트 HAL 포멧
 */

import com.uad2.application.member.entity.MemberExternalDto;
import org.springframework.hateoas.ResourceSupport;

import java.util.List;

public class MemberListExternalResource extends ResourceSupport {
    private List<MemberExternalDto> memberExternalDtoList;

    public MemberListExternalResource(List<MemberExternalDto> memberExternalDtoList){
        this.memberExternalDtoList = memberExternalDtoList;
    }

    public List<MemberExternalDto> getMemberList() {
        return memberExternalDtoList;
    }
}
