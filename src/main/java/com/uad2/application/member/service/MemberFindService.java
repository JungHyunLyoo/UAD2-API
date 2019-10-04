package com.uad2.application.member.service;/*
 * @USER zkdlwnfm
 * @DATE 2019-10-05
 */

import com.uad2.application.exception.ClientException;
import com.uad2.application.member.entity.Member;
import com.uad2.application.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MemberFindService {

    @Autowired
    private MemberRepository memberRepository;

    public Member findById(String id) {
        return Optional.ofNullable(memberRepository.findById(id))
                .orElseThrow(() -> new ClientException(String.format("Member(%s) is not exist", id)));
    }

    public Member findByPhoneNumber(String phoneNumber) {
        return Optional.ofNullable(memberRepository.findByPhoneNumber(phoneNumber))
                .orElseThrow(() -> new ClientException(String.format("Member(%s) is not exist", phoneNumber)));
    }

    public void isExistById(String id) {
        Optional.ofNullable(memberRepository.findById(id)).ifPresent(member -> { throw new ClientException(String.format("Id(%s) already exist", id)); });
    }

    public void isExistByPhoneNumber(String phoneNumber) {
        Optional.ofNullable(memberRepository.findByPhoneNumber(phoneNumber)).ifPresent(member -> { throw new ClientException(String.format("PhoneNumber(%s) already exist", phoneNumber)); });
    }
}
