package com.uad2.application.utils;
/*
 * @USER JungHyun
 * @DATE 2019-09-12
 * @DESCRIPTION 회원 API 반환 유틸
 */

import com.uad2.application.member.resource.MemberExternalResource;
import com.uad2.application.member.entity.MemberExternalDto;
import com.uad2.application.member.entity.Member;
import com.uad2.application.member.resource.MemberListExternalResource;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.Link;

import java.util.ArrayList;
import java.util.List;

public class MemberResponseUtil {

    private static ModelMapper modelMapper = new ModelMapper();

    /*
     * @USER JungHyun
     * @DATE 2019-09-12
     * @DESCRIPTION 회원 반환 resource 적용
     * @RESOURCE : HAL(반환 데이터에 관련 하이퍼링크를 제공하는 방식)을 적용한 반환 형식
     */
    public static MemberExternalResource makeResponseResource(Member member){
        MemberExternalDto memberExternalDto = modelMapper.map(member,MemberExternalDto.class);
        MemberExternalResource memberExternalResource = new MemberExternalResource(memberExternalDto);
        memberExternalResource.add(new Link("/docs/index.html") .withRel("profile"));
        return memberExternalResource;
    }

    /*
     * @USER JungHyun
     * @DATE 2019-09-12
     * @DESCRIPTION 회원 리스트 반환 resource 적용
     * @RESOURCE : HAL(반환 데이터에 관련 하이퍼링크를 제공하는 방식)을 적용한 반환 형식
     */
    public static MemberListExternalResource makeListResponseResource(List<Member> memberList){
        List<MemberExternalDto> memberExternalDtoList = new ArrayList<>();
        for (Member member : memberList) {
            memberExternalDtoList.add(modelMapper.map(member,MemberExternalDto.class));
        }
        MemberListExternalResource memberListExternalResource = new MemberListExternalResource(memberExternalDtoList);
        memberListExternalResource.add(new Link("/docs/index.html") .withRel("profile"));
        return memberListExternalResource;
    }
}
