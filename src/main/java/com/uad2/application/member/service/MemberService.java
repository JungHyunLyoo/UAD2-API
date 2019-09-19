package com.uad2.application.member.service;

import com.uad2.application.member.dto.MemberDto;
import com.uad2.application.member.entity.Member;
import com.uad2.application.member.repository.MemberRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public Member createMember(MemberDto.Request request) {
        String phoneNumber = request.getPhoneNumber();
        Member member = memberRepository.findByPhoneNumber(phoneNumber);
        if (member == null) {
            return memberRepository.save(modelMapper.map(request, Member.class));
        } else {
            return null;
        }
    }

}
