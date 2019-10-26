package com.uad2.application.attendance.controller;

import com.uad2.application.attendance.entity.Attendance;
import com.uad2.application.attendance.resource.AttendanceResponseUtil;
import com.uad2.application.attendance.service.AttendanceService;
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
    @GetMapping(value = "/api/attendance/memberSeq/{memberSeq}/date/{date}", produces = MediaTypes.HAL_JSON_UTF8_VALUE)
    public ResponseEntity getAttendance(@PathVariable int memberSeq,@PathVariable String date){
        List<Attendance> attendanceList = attendanceService.getAttendanceList(memberSeq,date);
        return ResponseEntity.ok().body(AttendanceResponseUtil.makeListResponseResource(attendanceList));
    }
}
