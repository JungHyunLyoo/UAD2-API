package com.uad2.application.attendance.controller;

import com.uad2.application.attendance.repository.AttendanceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AttendanceController {
    static final Logger logger = LoggerFactory.getLogger(AttendanceController.class);
    @Autowired
    private AttendanceRepository attendanceRepository;

}
