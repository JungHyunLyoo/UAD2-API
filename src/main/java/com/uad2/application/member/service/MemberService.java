package com.uad2.application.member.service;

import com.uad2.application.exception.ClientException;
import com.uad2.application.member.dto.MemberDto;
import com.uad2.application.member.entity.Member;
import com.uad2.application.member.repository.MemberRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.uad2.application.utils.EncryptUtil;
import org.springframework.util.ObjectUtils;

import java.util.List;

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

    public List<Member> getAllMember(){
        return memberRepository.findAll();
    }

    public Member getMemberById(String id) {
        return memberRepository.findById(id);
    }

    public Member createMember(MemberDto.Request requestMember) {
        return memberRepository.save(modelMapper.map(requestMember, Member.class));
    }

    public boolean isSamePwd(MemberDto.Request requestMember) {
        try{
            boolean isSamePwd;
            Member member = memberRepository.findById(requestMember.getId());
            isSamePwd = member.getPwd().equals(EncryptUtil.encryptMD5(requestMember.getPwd()));
            return isSamePwd;
        }catch (RuntimeException e){
            throw new RuntimeException("Error when getting pwd");
        }
    }

    public Member findByPhoneNumber(String phoneNumber) {
        return memberRepository.findByPhoneNumber(phoneNumber);
    }
    /*
    public void editProfile(MemberDto.EditRequest requestMember) throws ClientException {
        this.checkPwd(requestMember);
    }
    */
}
