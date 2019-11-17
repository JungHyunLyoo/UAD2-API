package com.uad2.application.matching.controller;

import com.uad2.application.attendance.entity.Attendance;
import com.uad2.application.attendance.service.AttendanceService;
import com.uad2.application.calculation.dto.CalculationDto;
import com.uad2.application.calculation.entity.Calculation;
import com.uad2.application.calculation.service.CalculationService;
import com.uad2.application.common.annotation.Auth;
import com.uad2.application.common.enumData.Role;
import com.uad2.application.exception.ClientException;
import com.uad2.application.matching.entity.Matching;
import com.uad2.application.matching.dto.MatchingDto;
import com.uad2.application.matching.resource.MatchingResponseUtil;
import com.uad2.application.matching.service.MatchingService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class MatchingController {
    static final Logger logger = LoggerFactory.getLogger(MatchingController.class);

    @Autowired
    AttendanceService attendanceService;

    @Autowired
    MatchingService matchingService;

    @Autowired
    CalculationService calculationService;

    @Autowired
    ModelMapper modelMapper;

    /**
     * 매칭 생성용 API
     * PostMapping : post 요청을 받아 해당 메소드와 매핑
     * produces : return 하는 형식
     */
    //@Auth(role = Role.ADMIN)
    @PostMapping(value = "/api/matching", produces = MediaTypes.HAL_JSON_UTF8_VALUE)
    public ResponseEntity createOrUpdateMatching(@RequestBody MatchingDto.Request requestMatching)
    {
        // 해당 일에 참가 가능 신청한 멤버들 참여정보 가져옴
        List<Attendance> attendanceAndMemberList = attendanceService.getAttendanceAndMemberListByDateAndTime(requestMatching.getMatchingDate().toString(), requestMatching.getMatchingTime());

        if (!attendanceAndMemberList.isEmpty()) {
            // 참가자들의 seq 와 phoneNum 추출
            ArrayList<String> attendanceMemberSeq = new ArrayList<>();
            attendanceAndMemberList.forEach( info -> attendanceMemberSeq.add(Integer.toString(info.getMember().getSeq())) );

            // 매칭이 이미 존재한다면 해당정보 가져와 업데이트(덮어씀), 없다면 새로 생성
            Matching matching = modelMapper.map(requestMatching, Matching.class);
            System.out.println("==========================");
            System.out.println(String.join(",", attendanceMemberSeq));
            matching.setAttendMember(String.join(",", attendanceMemberSeq));
            System.out.println(matching);
            System.out.println("==========================");
            Matching updatedMatching = matchingService.updateMatching(matching);




            CalculationDto.Request requestCalulation = CalculationDto.Request.builder()
                                                        .content(requestMatching.getMatchingPlace())
                                                        .price(requestMatching.getPrice())
                                                        .calculationDate(requestMatching.getMatchingDate()).build();
            Calculation calculation = calculationService.saveCalculation(requestCalulation,updatedMatching);

            return ResponseEntity.ok(updatedMatching);
        } else {
           throw new ClientException(String.format("Attendance member is not exist at that day(%s)", requestMatching.getMatchingDate()));
        }
    }

    @Auth(role = Role.USER)
    @GetMapping(value = "/api/matching/date/{date}", produces = MediaTypes.HAL_JSON_UTF8_VALUE)
    public ResponseEntity getMatching(@PathVariable String date){
        List<Matching> matchingList = matchingService.getMatchingByDate(date);
        return ResponseEntity.ok().body(MatchingResponseUtil.makeListResponseResource(matchingList));
    }


    //get matching
    //rmove matching
}
