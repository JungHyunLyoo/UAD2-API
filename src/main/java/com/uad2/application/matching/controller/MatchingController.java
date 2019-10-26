package com.uad2.application.matching.controller;

import com.uad2.application.attendance.entity.Attendance;
import com.uad2.application.attendance.service.AttendanceService;
import com.uad2.application.exception.ClientException;
import com.uad2.application.matching.entity.Matching;
import com.uad2.application.matching.dto.MatchingDto;
import com.uad2.application.matching.service.MatchingService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class MatchingController {
    static final Logger logger = LoggerFactory.getLogger(MatchingController.class);

    @Autowired
    AttendanceService attendanceService;

    @Autowired
    MatchingService matchingService;

    @Autowired
    ModelMapper modelMapper;

    /**
     * 매칭 생성용 API
     * PostMapping : post 요청을 받아 해당 메소드와 매핑
     * produces : return 하는 형식
     */
    @PostMapping(value = "/api/matching", produces = MediaTypes.HAL_JSON_UTF8_VALUE)
    public ResponseEntity createMatching(@RequestBody MatchingDto.Request requestMatching)
    {
        // 해당 일에 참가 가능 신청한 멤버들 참여정보 가져옴
        List<Attendance> attendanceAndMemberList = Optional.ofNullable(attendanceService.getAttendanceAndMemberListByDateAndTime(requestMatching.getMatchingDate(), requestMatching.getMatchingTime()))
                .orElseThrow(() -> new ClientException(String.format("Attendance member is not exist at that day(%s)", requestMatching.getMatchingDate())));

        // 참가자들의 seq 와 phoneNum 추출
        ArrayList<Integer> attendanceMemberSeq = new ArrayList<>();
        ArrayList<String> attendanceMemberPhoneNum = new ArrayList<>();
        attendanceAndMemberList.forEach( info -> {
            attendanceMemberSeq.add(info.getMember().getSeq());
            //attendanceMemberPhoneNum.add(info.getMember().getPhoneNumber());
        });

        // 매칭이 이미 존재한다면 해당정보 가져와 업데이트(덮어씀), 없다면 새로 생성
        Matching matching =Optional.ofNullable(matchingService.getMatchingByDate(requestMatching.getMatchingDate())).orElse(new Matching());
        matching.setAttendMember(String.join(",", attendanceMemberSeq.toString()));
        matching.setMatchingPlace(requestMatching.getMatchingPlace());
        matching.setContent(requestMatching.getContent());
        matching.setMatchingDate(requestMatching.getMatchingDate());
        matching.setMatchingTime(requestMatching.getMatchingTime());
        matching.setMaxCnt(requestMatching.getMaxCnt());
        matchingService.updateMatching(matching);


        System.out.println("-----------------------------------------");
        System.out.println(matching.toString());
        System.out.println("-----------------------------------------");

        // 정산정보에 추가/업데이트

        return ResponseEntity.ok().build();
    }
}
