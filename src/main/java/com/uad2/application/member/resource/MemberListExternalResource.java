package com.uad2.application.member.resource;
/*
 * @USER JungHyun
 * @DATE 2019-09-12
 * @DESCRIPTION api 반환용 회원 리스트 HAL 포멧
 */

import com.uad2.application.member.dto.MemberDto;
import org.springframework.hateoas.ResourceSupport;

import java.util.List;

public class MemberListExternalResource extends ResourceSupport {
    private List<MemberDto.Response> responseList;

    private MemberListExternalResource(List<MemberDto.Response> responseList) {
        this.responseList = responseList;
    }

    /**
     * 인스턴스 생성을 위한 정적 팩토리 메서드
     *
     * @param responseList 회원 Response List 커맨드 객체
     * @return MemberListExternalResource 인스턴스를 반환한다.
     */
    public static MemberListExternalResource from(List<MemberDto.Response> responseList) {
        return new MemberListExternalResource(responseList);
    }

    public List<MemberDto.Response> getMemberList() {
        return responseList;
    }
}
