package com.uad2.application.controller;

import com.uad2.application.entity.Member;
import com.uad2.application.repository.MemberRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class MemberController {
    static final Logger logger = LoggerFactory.getLogger(MemberController.class);

    @Autowired
    private MemberRepository memberRepository;

    @PostMapping("/member")
    public Object Member(@RequestBody  Member member){
        logger.debug(member.toString());
        memberRepository.save(member);
        return "testtest";
    }
}
