package com.uad2.application.matching.resource;
/*
 * @USER JungHyun
 * @DATE 2019-10-06
 * @DESCRIPTION api 반환용 회원 리스트 HAL 포멧
 */

import com.uad2.application.attendance.dto.AttendanceDto;
import com.uad2.application.matching.dto.MatchingDto;
import com.uad2.application.matching.entity.Matching;
import org.springframework.hateoas.ResourceSupport;

import java.util.List;

public class MatchingListExternalResource extends ResourceSupport {
    private List<MatchingDto.Response> responseList;

    private MatchingListExternalResource(List<MatchingDto.Response> responseList) {
        this.responseList = responseList;
    }

    /**
     * 인스턴스 생성을 위한 정적 팩토리 메서드
     *
     * @param responseList 회원 Response List 커맨드 객체
     * @return MemberListExternalResource 인스턴스를 반환한다.
     */
    public static MatchingListExternalResource createResourceFrom(List<MatchingDto.Response> responseList) {
        return new MatchingListExternalResource(responseList);
    }

    public List<MatchingDto.Response> getAttendanceList() {
        return responseList;
    }
}
