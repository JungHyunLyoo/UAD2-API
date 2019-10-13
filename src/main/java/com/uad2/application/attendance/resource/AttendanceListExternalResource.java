package com.uad2.application.attendance.resource;
/*
 * @USER JungHyun
 * @DATE 2019-10-06
 * @DESCRIPTION api 반환용 회원 리스트 HAL 포멧
 */

import com.uad2.application.attendance.dto.AttendanceDto;
import com.uad2.application.member.dto.MemberDto;
import org.springframework.hateoas.ResourceSupport;

import java.util.List;

public class AttendanceListExternalResource extends ResourceSupport {
    private List<AttendanceDto.Response> responseList;

    private AttendanceListExternalResource(List<AttendanceDto.Response> responseList) {
        this.responseList = responseList;
    }

    /**
     * 인스턴스 생성을 위한 정적 팩토리 메서드
     *
     * @param responseList 회원 Response List 커맨드 객체
     * @return MemberListExternalResource 인스턴스를 반환한다.
     */
    public static AttendanceListExternalResource createResourceFrom(List<AttendanceDto.Response> responseList) {
        return new AttendanceListExternalResource(responseList);
    }

    public List<AttendanceDto.Response> getAttendanceList() {
        return responseList;
    }
}
