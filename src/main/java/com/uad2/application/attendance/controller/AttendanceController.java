package com.uad2.application.attendance.controller;

import com.uad2.application.attendance.dto.AttendanceDto;
import com.uad2.application.attendance.entity.Attendance;
import com.uad2.application.attendance.repository.AttendanceRepository;
import com.uad2.application.attendance.resource.AttendanceResponseUtil;
import com.uad2.application.attendance.service.AttendanceService;
import com.uad2.application.common.annotation.Auth;
import com.uad2.application.common.enumData.Role;
import com.uad2.application.matching.entity.Matching;
import com.uad2.application.matching.service.MatchingService;
import com.uad2.application.member.entity.Member;
import com.uad2.application.member.service.MemberService;
import com.uad2.application.utils.SessionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.sql.Date;
import java.util.List;
import java.util.Objects;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;


@RestController
public class AttendanceController {
    static final Logger logger = LoggerFactory.getLogger(AttendanceController.class);

    @Autowired
    AttendanceService attendanceService;

    @Autowired
    MatchingService matchingService;

    @Autowired
    MemberService memberService;

    @Autowired
    AttendanceRepository attendanceRepository;


    @Auth(role = Role.USER)
    @GetMapping(value = "/api/attendance/date/{date}", produces = MediaTypes.HAL_JSON_UTF8_VALUE)
    public ResponseEntity getAllAttendanceList(@PathVariable String date){
        List<Attendance> attendanceList = attendanceService.getAllAttendanceList(date);
        return ResponseEntity.ok().body(AttendanceResponseUtil.makeListResponseResource(attendanceList));
    }

    @Auth(role = Role.USER)
    @GetMapping(value = "/api/attendance/memberSeq/{memberSeq}/date/{date}", produces = MediaTypes.HAL_JSON_UTF8_VALUE)
    public ResponseEntity getAttendanceList(@PathVariable int memberSeq,@PathVariable String date){
        Attendance attendanceList = attendanceService.getAttendance(memberSeq,date);
        return ResponseEntity.ok().body(AttendanceResponseUtil.makeResponseResource(attendanceList));
    }

    @Auth(role = Role.USER)
    @PostMapping(value = "/api/attendance", produces = MediaTypes.HAL_JSON_UTF8_VALUE)
    public ResponseEntity createAttendance(HttpServletRequest request, @RequestBody AttendanceDto.Request attendanceRequest){

        String availableTime = attendanceRequest.getAvailableTime();
        String availableDate = attendanceRequest.getAvailableDate();

        //updateMatchingInfo
        //availabledate에 해당하는 매칭이 있을 경우만 취급
        List<Matching> matchingList = matchingService.getMatchingByDate(availableDate);
        if(Objects.nonNull(matchingList) && matchingList.size() > 0){
            Matching matching = matchingList.get(0);
            if(Objects.nonNull(matching)){
                if(availableTime.contains(matching.getMatchingTime()) &&
                        !matching.getAttendMember().contains(Integer.toString(attendanceRequest.getMemberSeq()))){
                    matching.setAttendMember(matching.getAttendMember() + "," + attendanceRequest.getMemberSeq());
                }
                else if(!availableTime.contains(matching.getMatchingTime()) &&
                        matching.getAttendMember().contains(Integer.toString(attendanceRequest.getMemberSeq()))){
                    matching.setAttendMember(matching.getAttendMember().replace("," + attendanceRequest.getMemberSeq(),""));
                    matching.setAttendMember(matching.getAttendMember().replace("," + attendanceRequest.getMemberSeq(),""));
                    matching.setAttendMember(matching.getAttendMember().replace("," + attendanceRequest.getMemberSeq(),""));
                }
                //나머지 4개의 경우는...??
            }
        }


        Attendance attendance = attendanceService.getAttendance(attendanceRequest.getMemberSeq(),attendanceRequest.getAvailableDate());
        if(Objects.isNull(availableTime)){
            if(Objects.nonNull(attendance)){
                attendanceRepository.delete(attendance);
                return ResponseEntity.ok().build();
            }
        }
        else{
            if(Objects.nonNull(attendance)){
                attendance.setAvailableTime(availableTime);
                attendanceRepository.save(attendance);
                return ResponseEntity.ok().build();
            }
            else{
                Member member = (Member)SessionUtil.getAttribute(request.getSession(),"member");
                Attendance attendanceNew = Attendance.builder()
                        .member(member)
                        .availableDate(Date.valueOf(availableDate))
                        .availableTime(availableTime).build();
                attendanceRepository.save(attendanceNew);


                URI createdUri = linkTo(AttendanceController.class).slash("seq").slash(attendanceNew.getSeq()).toUri();
                return ResponseEntity.created(createdUri).body(AttendanceResponseUtil.makeResponseResource(attendance));
            }
        }
        return ResponseEntity.ok().build();
        /*
        //신청한 시간대에 5명 이상의 참여자가 있을 경우, 회장에게 메세지 발송
        String[] matchingMemberList = matching.getAttendMember().split(",");
        for(int i=0;i<matchingMemberList.length-1;i++){
            String prev = matchingMemberList[i];
            String next = matchingMemberList[i+1];
            if(prev+1 == next){
                String time = prev+","+next;
                List<Attendance> attendanceList = attendanceService.getAttendanceAndMemberListByDateAndTime(availableDate,availableTime);
                if(attendanceList.size() >= 5){
                    //$messageController=new MessageController();
                    //$messageText=$available_date.'일, '.$checkHour.'시간대 매칭 필요';
                    //$messageController->sendMessage('01094736496',$messageText);
                }
            }
        }
         */
    }

}
