package com.uad2.application.matching.resource;
import com.uad2.application.attendance.dto.AttendanceDto;
import com.uad2.application.attendance.entity.Attendance;
import com.uad2.application.matching.dto.MatchingDto;
import com.uad2.application.matching.entity.Matching;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.Link;

import java.util.List;
import java.util.stream.Collectors;

/*
 * @USER JungHyun
 * @DATE 2019-10-06
 * @DESCRIPTION
 */
public class MatchingResponseUtil {

    private static ModelMapper modelMapper = new ModelMapper();

    /*
     * @USER JungHyun
     * @DATE 2019-10-06
     * @DESCRIPTION 참가 반환 resource 적용
     * @RESOURCE : HAL(반환 데이터에 관련 하이퍼링크를 제공하는 방식)을 적용한 반환 형식
     */
    public static MatchingExternalResource makeResponseResource(Matching matching) {
        MatchingDto.Response response = matching == null ? null : modelMapper.map(matching, MatchingDto.Response.class);
        MatchingExternalResource matchingExternalResource = MatchingExternalResource.createResourceFrom(response);
        matchingExternalResource.add(new Link("/docs/index.html").withRel("profile"));
        return matchingExternalResource;
    }

    /*
     * @USER JungHyun
     * @DATE 2019-10-06
     * @DESCRIPTION 참가 리스트 반환 resource 적용
     * @RESOURCE : HAL(반환 데이터에 관련 하이퍼링크를 제공하는 방식)을 적용한 반환 형식
     */
    public static MatchingListExternalResource makeListResponseResource(List<Matching> matchingList) {
        List<MatchingDto.Response> responseList = matchingList == null ? null : matchingList.stream()
                .map(matching -> modelMapper.map(matching, MatchingDto.Response.class))
                .collect(Collectors.toList());
        MatchingListExternalResource memberListExternalResource = MatchingListExternalResource.createResourceFrom(responseList);
        memberListExternalResource.add(new Link("/docs/index.html").withRel("profile"));
        return memberListExternalResource;
    }
}
