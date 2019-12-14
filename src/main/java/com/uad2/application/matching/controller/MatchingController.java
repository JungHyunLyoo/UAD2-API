package com.uad2.application.matching.controller;

import com.uad2.application.attendance.entity.Attendance;
import com.uad2.application.attendance.service.AttendanceService;
import com.uad2.application.calculation.dto.CalculationDto;
import com.uad2.application.calculation.entity.Calculation;
import com.uad2.application.calculation.service.CalculationService;
import com.uad2.application.common.annotation.Auth;
import com.uad2.application.common.enumData.Role;
import com.uad2.application.exception.ClientException;
import com.uad2.application.matching.MatchingValidator;
import com.uad2.application.matching.entity.Matching;
import com.uad2.application.matching.dto.MatchingDto;
import com.uad2.application.matching.resource.MatchingResponseUtil;
import com.uad2.application.matching.service.MatchingService;
import com.uad2.application.member.controller.MemberController;
import com.uad2.application.member.resource.MemberResponseUtil;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@RestController
public class MatchingController {
    static final Logger logger = LoggerFactory.getLogger(MatchingController.class);

    private final AttendanceService attendanceService;
    private final MatchingService matchingService;
    private final CalculationService calculationService;
    private final ModelMapper modelMapper;
    private final MatchingValidator matchingValidator;
    @Autowired
    public MatchingController(AttendanceService attendanceService,
                              MatchingService matchingService,
                              CalculationService calculationService,
                              ModelMapper modelMapper,
                              MatchingValidator matchingValidator){
        this.attendanceService = attendanceService;
        this.matchingService =  matchingService;
        this.calculationService = calculationService;
        this.modelMapper = modelMapper;
        this.matchingValidator = matchingValidator;
    }


    /**
     * 매칭 생성용 API
     * PostMapping : post 요청을 받아 해당 메소드와 매핑
     * produces : return 하는 형식
     */
    @Auth(role = Role.ADMIN)
    @PostMapping(value = "/api/matching", produces = MediaTypes.HAL_JSON_UTF8_VALUE)
    public ResponseEntity createMatching(@RequestBody MatchingDto.Request requestMatching)
    {
        matchingValidator.validateCreateMatching(requestMatching);

        String requestDate = requestMatching.getMatchingDate();
        String requestPlace = requestMatching.getMatchingPlace();
        String requestContent = requestMatching.getContent();
        int requestPrice = requestMatching.getPrice();
        //매칭 시간대에 참여자 리스트 추출
        List<Attendance> attendanceAndMemberList = attendanceService.getAttendanceAndMemberListByDateAndTime(requestDate, requestMatching.getMatchingTime());

        if (!attendanceAndMemberList.isEmpty()) {
            // 참가자들의 seq 와 phoneNum 추출
            ArrayList<String> attendanceMemberSeq = new ArrayList<>();
            attendanceAndMemberList.forEach( info -> attendanceMemberSeq.add(Integer.toString(info.getMember().getSeq())) );
            List<Matching> matchingList = matchingService.getMatchingByDate(requestDate);
            Matching matching;
            if(0 < matchingList.size()){
                matching = matchingList.get(0);
            }
            else{
                matching = modelMapper.map(requestMatching, Matching.class);
            }
            matching.setAttendMember(String.join(",", attendanceMemberSeq));
            matching.setMatchingPlace(requestPlace);
            matching.setContent(requestContent);
            Matching updatedMatching = matchingService.updateMatching(matching);


            List<Calculation> calculationList = calculationService.getCalculationListByCalculationDate(requestDate);
            Calculation calculation;
            if(0 < calculationList.size()){
                calculation = calculationList.get(0);
            }
            else{
                calculation = new Calculation();
            }
            calculation.setContent(requestPlace);
            calculation.setPrice(requestPrice);
            calculation.setCalculationDate(requestDate);
            calculation.setMatching(updatedMatching);
            calculation.setKind(0);
            calculation.setAttendCnt(0);
            calculationService.saveCalculation(calculation);

            URI createdUri = linkTo(MemberController.class).toUri();
            return ResponseEntity.created(createdUri).body(MatchingResponseUtil.makeResponseResource(updatedMatching));
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

}
