package com.uad2.application;

import com.uad2.application.domain.Member;
import com.uad2.application.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TestClass {
    @Autowired
    private MemberRepository memberRepository;
    @GetMapping("/test")
    public String test(){
        List<Member> memberList = memberRepository.findAll();
        System.out.println(memberList.size());
        return "test";
    }
}
