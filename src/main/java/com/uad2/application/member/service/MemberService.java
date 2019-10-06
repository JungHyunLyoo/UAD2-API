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

    public List<Member> getAllMember(){
        return Optional.ofNullable(memberRepository.findAll())
                .orElseThrow(() -> new ClientException("No member is exist"));
    }

    public Member getMemberById(String id) {
        return Optional.ofNullable(memberRepository.findById(id))
                .orElseThrow(() -> new ClientException(String.format("Member(%s) is not exist", id)));
    }

    public Member createMember(MemberDto.Request requestMember) {
        String phoneNumber = requestMember.getPhoneNumber();
        if(ObjectUtils.isEmpty(memberRepository.findByPhoneNumber(phoneNumber))){
            throw new ClientException(String.format("PhoneNumber(%s) already exist", phoneNumber));
        }
        return memberRepository.save(modelMapper.map(requestMember, Member.class));
    }

    public void checkPwd(MemberDto.Request requestMember) throws ClientException {
        Member member = this.getMemberById(requestMember.getId());

        if (!member.getPwd().equals(EncryptUtil.encryptMD5(requestMember.getPwd()))) {
            throw new ClientException("Password not matched");
        }
    }

    public void checkId(String id) throws ClientException {
        Optional.ofNullable(memberRepository.findById(id)).ifPresent(member -> { throw new ClientException(String.format("Id(%s) already exist", id)); });
    }

    public Member findByPhoneNumber(String phoneNumber) {
        return Optional.ofNullable(memberRepository.findByPhoneNumber(phoneNumber))
                .orElseThrow(() -> new ClientException(String.format("Member(%s) is not exist", phoneNumber)));
    }
    /*
    public void editProfile(MemberDto.EditRequest requestMember) throws ClientException {
        this.checkPwd(requestMember);
    }
    */
}
