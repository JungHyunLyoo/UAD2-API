package com.uad2.application.attendance.controller;

import com.uad2.application.attendance.entity.Attendance;
import com.uad2.application.attendance.resource.AttendanceResponseUtil;
import com.uad2.application.attendance.service.AttendanceService;
import com.uad2.application.common.annotation.Auth;
import com.uad2.application.common.enumData.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
public class AttendanceController {
    static final Logger logger = LoggerFactory.getLogger(AttendanceController.class);

    @Autowired
    AttendanceService attendanceService;


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
    @PostMapping(value = "", produces = MediaTypes.HAL_JSON_UTF8_VALUE)
    public ResponseEntity createAttendance(){
        //get memseq
        //get availabletime
        //get availabledate

        //updateMatchingInfo
        //availabledate에 해당하는 매칭이 있을 경우만 취급
        //availabletime에 해당 매칭의 시간이 포함되고 그 매칭에 내가 없을 경우->그 매칭에 나 추가시킴
        //availabletime에 해당 매칭의 시간이 포함되지 않고 그 매칭에 내가 있을 경우->그 매칭에 나 제거함

        //availabletime가 null이고 해당 월에 이전 참가 내역이 있으면 그 내역 제거
        //availabletime가 null이 아니고 해당 월에 이전 참가 내역이 있으면 그 내역 갱신
        //availabletime가 null이 아니고 해당 월에 이전 참가 내역이 없으면 새로 생성

        //신청한 시간대에 5명 이상의 참여자가 있을 경우, 회장에게 메세지 발송
    }*/
}
