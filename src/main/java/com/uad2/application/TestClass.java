package com.uad2.application;

import com.uad2.application.domain.Member;
import com.uad2.application.repository.MemberRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TestClass {
    static final Logger logger = LoggerFactory.getLogger(TestClass.class);
    @Autowired
    private MemberRepository memberRepository;
    @GetMapping("/test")
    public String test(){
        List<Member> memberList = memberRepository.findAll();
        logger.info("info ID : {}", "foo");
        logger.debug("debug ID : {}", "foo");
        System.out.println("!23");
        return "test";
    }
}
