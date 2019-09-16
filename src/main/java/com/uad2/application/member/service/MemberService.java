package com.uad2.application.member.service;

import com.uad2.application.member.controller.MemberController;
import com.uad2.application.member.entity.Member;
import com.uad2.application.member.entity.MemberInsertDto;
import com.uad2.application.member.repository.MemberRepository;
import com.uad2.application.utils.MemberResponseUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.xml.ws.Response;
import java.net.URI;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

/*
 * @USER JungHyun
 * @DATE 2019-09-10
 */
@Service
public class MemberService {
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ModelMapper modelMapper;
    public Member createMember(MemberInsertDto memberInsertDto){
        String phoneNumber = memberInsertDto.getPhoneNumber();
        Member member = memberRepository.findByPhoneNumber(phoneNumber);
        if(member == null){
            return memberRepository.save(modelMapper.map(memberInsertDto,Member.class));
        }
        else{
            return null;
        }
    }

}
