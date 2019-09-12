package com.uad2.application.utils;
/*
 * @USER JungHyun
 * @DATE 2019-09-12
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

    public static Object makeResponseResource(Member member){
        MemberExternalDto memberExternalDto = modelMapper.map(member,MemberExternalDto.class);
        MemberExternalResource memberExternalResource = new MemberExternalResource(memberExternalDto);
        memberExternalResource.add(new Link("/docs/index.html") .withRel("profile"));
        return memberExternalResource;
    }

    public static Object makeListResponseResource(List<Member> memberList){
        List<MemberExternalDto> memberExternalDtoList = new ArrayList<>();
        for (Member member : memberList) {
            memberExternalDtoList.add(modelMapper.map(member,MemberExternalDto.class));
        }
        MemberListExternalResource memberListExternalResource = new MemberListExternalResource(memberExternalDtoList);
        memberListExternalResource.add(new Link("/docs/index.html") .withRel("profile"));
        return memberListExternalResource;
    }
}
