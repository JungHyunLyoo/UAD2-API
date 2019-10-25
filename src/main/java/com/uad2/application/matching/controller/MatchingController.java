package com.uad2.application.matching.controller;

import com.uad2.application.matching.entity.Matching;
import com.uad2.application.matching.dto.MatchingDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MatchingController {
    static final Logger logger = LoggerFactory.getLogger(MatchingController.class);

    /**
     * 매칭 생성용 API
     * PostMapping : get 요청을 받아 해당 메소드와 매핑
     * produces : return 하는 형식
     */

    @PostMapping(value = "/api/matching", produces = MediaTypes.HAL_JSON_UTF8_VALUE)
    public ResponseEntity createMatching(@RequestBody MatchingDto.Request requestMatching)
    {

    }
}
