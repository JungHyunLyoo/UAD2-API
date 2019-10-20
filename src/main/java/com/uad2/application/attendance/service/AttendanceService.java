package com.uad2.application.attendance.service;
/*
 * @USER JungHyun
 * @DATE 2019-10-05
 * @DESCRIPTION
 */

import com.uad2.application.attendance.entity.Attendance;
import com.uad2.application.attendance.repository.AttendanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class AttendanceService {
    @Autowired
    private AttendanceRepository attendanceRepository;

    public List<Attendance> getAttendanceList(int memberSeq,String date){
        return attendanceRepository.findByMemberSeqAndAvailableDate(memberSeq,date);
    }

    public List<Attendance> getAttendanceAndMemberListByDateAndTime(Date date, String time) {
        return attendanceRepository.findByAvailableDateAndTime(date, time);
    }
}
