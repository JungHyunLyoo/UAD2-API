package com.uad2.application.attendance.controller;

import com.uad2.application.attendance.dto.AttendanceDto;
import com.uad2.application.attendance.entity.Attendance;
import com.uad2.application.attendance.resource.AttendanceResponseUtil;
import com.uad2.application.attendance.service.AttendanceService;
import com.uad2.application.calculation.dto.CalculationDto;
import com.uad2.application.common.annotation.Auth;
import com.uad2.application.common.enumData.Role;
import com.uad2.application.matching.entity.Matching;
import com.uad2.application.matching.service.MatchingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Objects;


@RestController
public class AttendanceController {
    static final Logger logger = LoggerFactory.getLogger(AttendanceController.class);

    @Autowired
    AttendanceService attendanceService;

    @Autowired
    MatchingService matchingService;


    @Auth(role = Role.USER)
    @GetMapping(value = "/api/attendance/date/{date}", produces = MediaTypes.HAL_JSON_UTF8_VALUE)
    public ResponseEntity getAllAttendanceList(@PathVariable String date){
        List<Attendance> attendanceList = attendanceService.getAllAttendanceList(date);
        return ResponseEntity.ok().body(AttendanceResponseUtil.makeListResponseResource(attendanceList));
    }

    @Auth(role = Role.USER)
    @GetMapping(value = "/api/attendance/memberSeq/{memberSeq}/date/{date}", produces = MediaTypes.HAL_JSON_UTF8_VALUE)
    public ResponseEntity getAttendanceList(@PathVariable int memberSeq,@PathVariable String date){
        List<Attendance> attendanceList = attendanceService.getAttendanceList(memberSeq,date);
        return ResponseEntity.ok().body(AttendanceResponseUtil.makeListResponseResource(attendanceList));
    }

    /*
    @PostMapping(value = "/api/attendance", produces = MediaTypes.HAL_JSON_UTF8_VALUE)
    public ResponseEntity createAttendance(@RequestBody AttendanceDto.Request request){

        String availableTime = request.getAvailableTime();
        Date availableDate = request.getAvailableDate();

        //updateMatchingInfo
        //availabledate에 해당하는 매칭이 있을 경우만 취급
        Matching matching = matchingService.getMatchingByDate(availableDate);
        if(Objects.nonNull(matching)){
            if(availableTime.contains(matching.getMatchingTime()) &&
                !matching.getAttendMember().contains(request.getMemberSeq())){
                matching.setAttendMember(matching.getAttendMember() + request.getMemberSeq());
            }
            else if(!availableTime.contains(matching.getMatchingTime()) &&
                    matching.getAttendMember().contains(request.getMemberSeq())){
                matching.setAttendMember(matching.getAttendMember() - request.getMemberSeq());
            }
            //나머지 4개의 경우는...??
        }

        if(Objects.isNull(availableTime)){
            List<Attendance> attendanceList = attendanceService.getAllAttendanceList(availableDate);
            if(attendanceList.contains(request.getMemberSeq())){
                //remove
            }
        }
        else{
            List<Attendance> attendanceList = attendanceService.getAllAttendanceList(availableDate);
            if(attendanceList.contains(request.getMemberSeq())){
                //update
            }
            else{
                //insert
            }
        }

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
    }
     */
}
