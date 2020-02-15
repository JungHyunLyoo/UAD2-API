package com.uad2.application.overseas.controller;

import com.uad2.application.common.annotation.Auth;
import com.uad2.application.common.enumData.Role;
import com.uad2.application.overseas.dto.OverseasFootballDto;
import com.uad2.application.overseas.service.OverseasFootballService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/*
 * @USER Jongyeob Kim
 * @DATE 2020-01-17
 */
@RestController
public class OverseasFootballController {

    @Autowired
    private OverseasFootballService overseasFootballService;

    @Auth(role = Role.USER)
    @GetMapping("/api/overseas/football")
    public ResponseEntity<OverseasFootballDto> getOverseasFootballInfo() {
        return ResponseEntity.ok().body(OverseasFootballDto.of(overseasFootballService.getLeagueInfo()));
    }

}
