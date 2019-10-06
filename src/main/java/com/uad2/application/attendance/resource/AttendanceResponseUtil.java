package com.uad2.application.attendance.resource;
import com.uad2.application.attendance.dto.AttendanceDto;
import com.uad2.application.attendance.entity.Attendance;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.Link;

import java.util.List;
import java.util.stream.Collectors;

/*
 * @USER JungHyun
 * @DATE 2019-10-06
 * @DESCRIPTION
 */
public class AttendanceResponseUtil {

    private static ModelMapper modelMapper = new ModelMapper();

    /*
     * @USER JungHyun
     * @DATE 2019-10-06
     * @DESCRIPTION 참가 반환 resource 적용
     * @RESOURCE : HAL(반환 데이터에 관련 하이퍼링크를 제공하는 방식)을 적용한 반환 형식
     */
    public static AttendanceExternalResource makeResponseResource(Attendance attendance) {
        AttendanceDto.Response response = modelMapper.map(attendance, AttendanceDto.Response.class);
        AttendanceExternalResource attendanceExternalResource = AttendanceExternalResource.createResourceFrom(response);
        attendanceExternalResource.add(new Link("/docs/index.html").withRel("profile"));
        return attendanceExternalResource;
    }

    /*
     * @USER JungHyun
     * @DATE 2019-10-06
     * @DESCRIPTION 참가 리스트 반환 resource 적용
     * @RESOURCE : HAL(반환 데이터에 관련 하이퍼링크를 제공하는 방식)을 적용한 반환 형식
     */
    public static AttendanceListExternalResource makeListResponseResource(List<Attendance> attendanceList) {
        List<AttendanceDto.Response> responseList = attendanceList.stream()
                .map(attendance -> modelMapper.map(attendance, AttendanceDto.Response.class))
                .collect(Collectors.toList());
        AttendanceListExternalResource memberListExternalResource = AttendanceListExternalResource.createResourceFrom(responseList);
        memberListExternalResource.add(new Link("/docs/index.html").withRel("profile"));
        return memberListExternalResource;
    }
}
