package com.uad2.application.member.resource;
/*
 * @USER JungHyun
 * @DATE 2019-09-12
 * @DESCRIPTION 회원 API 반환 유틸
 */

import com.uad2.application.member.dto.MemberDto;
import com.uad2.application.member.entity.Member;
import com.uad2.application.member.resource.MemberExternalResource;
import com.uad2.application.member.resource.MemberListExternalResource;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.Link;

import java.util.List;
import java.util.stream.Collectors;

public class MemberResponseUtil {

    private static ModelMapper modelMapper = new ModelMapper();

    /*
     * @USER JungHyun
     * @DATE 2019-09-12
     * @DESCRIPTION 회원 반환 resource 적용
     * @RESOURCE : HAL(반환 데이터에 관련 하이퍼링크를 제공하는 방식)을 적용한 반환 형식
     */
    public static MemberExternalResource makeResponseResource(Member member) {
        MemberDto.Response response = modelMapper.map(member, MemberDto.Response.class);
        MemberExternalResource memberExternalResource = MemberExternalResource.createResourceFrom(response);
        memberExternalResource.add(new Link("/docs/index.html").withRel("profile"));
        return memberExternalResource;
    }

    /*
     * @USER JungHyun
     * @DATE 2019-09-12
     * @DESCRIPTION 회원 리스트 반환 resource 적용
     * @RESOURCE : HAL(반환 데이터에 관련 하이퍼링크를 제공하는 방식)을 적용한 반환 형식
     */
    public static MemberListExternalResource makeListResponseResource(List<Member> memberList) {
        List<MemberDto.Response> responseList = memberList.stream()
                .map(member -> modelMapper.map(member, MemberDto.Response.class))
                .collect(Collectors.toList());
        MemberListExternalResource memberListExternalResource = MemberListExternalResource.createResourceFrom(responseList);
        memberListExternalResource.add(new Link("/docs/index.html").withRel("profile"));
        return memberListExternalResource;
    }
}
