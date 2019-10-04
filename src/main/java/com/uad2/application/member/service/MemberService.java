package com.uad2.application.member.service;

import com.uad2.application.exception.ClientException;
import com.uad2.application.member.dto.MemberDto;
import com.uad2.application.member.entity.Member;
import com.uad2.application.member.repository.MemberRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.uad2.application.utils.EncryptUtil;

import java.util.Optional;

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

    @Autowired
    private MemberFindService memberFindService;


    public Member createMember(MemberDto.Request requestMember) throws ClientException {
        memberFindService.isExistByPhoneNumber(requestMember.getPhoneNumber());

        return memberRepository.save(modelMapper.map(requestMember, Member.class));
    }

    public void checkPwd(MemberDto.Request requestMember) throws ClientException {
        Member member = memberFindService.findById(requestMember.getId());

        // check password equals
        if (!member.getPwd().equals(EncryptUtil.encryptMD5(requestMember.getPwd()))) {
            throw new ClientException("Password not matched");
        }
    }

    public void checkId(String id) throws ClientException {
        memberFindService.isExistById(id);
    }

    /*public void editProfile(MemberDto.EditRequest requestMember) throws ClientException {
        this.checkPwd(requestMember);
    }*/
}
