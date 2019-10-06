package com.uad2.application.member.controller;

import com.uad2.application.exception.ClientException;
import com.uad2.application.member.MemberValidator;
import com.uad2.application.member.dto.MemberDto;
import com.uad2.application.member.entity.Member;
import com.uad2.application.member.service.MemberService;
import com.uad2.application.member.resource.MemberResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@RestController
public class MemberController {
    static final Logger logger = LoggerFactory.getLogger(MemberController.class);

    @Autowired
    MemberValidator memberValidator;

    @Autowired
    MemberService memberService;


    /**
     * 회원 전체 조회 API
     * GetMapping : get 요청을 받아 해당 메소드와 매핑
     * produces : return 하는 형식
     */
    @GetMapping(value = "/api/member", produces = MediaTypes.HAL_JSON_UTF8_VALUE)
    public ResponseEntity getAllMember() {
        List<Member> memberList = Optional.ofNullable(memberService.getAllMember())
                .orElseThrow(() -> new ClientException("Member is not exist"));
        return ResponseEntity.ok(MemberResponseUtil.makeListResponseResource(memberList));
    }

    /**
     * 회원 id 조회 API
     */
    @GetMapping(value = "/api/member/id/{id}", produces = MediaTypes.HAL_JSON_UTF8_VALUE)
    public ResponseEntity getMemberById(@PathVariable String id) {
        Member member = Optional.ofNullable(memberService.getMemberById(id))
                .orElseThrow(() -> new ClientException("Member is not exist"));
        return ResponseEntity.ok(MemberResponseUtil.makeResponseResource(member));
    }

    /**
     * 회원 생성 API
     */
    @PostMapping(value = "/api/member", produces = MediaTypes.HAL_JSON_UTF8_VALUE)
    public ResponseEntity createMember(@RequestBody MemberDto.Request requestMember) {
        memberValidator.validateCreateMember(requestMember);

        String phoneNumber = requestMember.getPhoneNumber();
        if(!ObjectUtils.isEmpty(memberService.findByPhoneNumber(phoneNumber))){
            throw new ClientException(String.format("PhoneNumber(%s) already exist", phoneNumber));
        }

        Member savedMember = memberService.createMember(requestMember);
        URI createdUri = linkTo(MemberController.class).slash("id").slash(requestMember.getId()).toUri();
        return ResponseEntity.created(createdUri).body(MemberResponseUtil.makeResponseResource(savedMember));
    }

    /**
     * 비밀번호 체크 - 프로필 수정 검증용
     */
    @PostMapping(value = "/api/member/checkPwd", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity checkPwd(@RequestBody MemberDto.Request requestMember) {
        Map<String,Object> returnMap = new HashMap<>();
        returnMap.put("isSamePwd",memberService.isSamePwd(requestMember));
        return ResponseEntity.ok().body(returnMap);
    }

    /**
     * 아이디 중복 체크 - 회원가입시 별도 체크용
     */
    @GetMapping(value = "/api/member/checkId/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity checkId(@PathVariable String id) {
        Map<String,Object> returnMap = new HashMap<>();
        returnMap.put("alreadyExistId",memberService.isExistMemberById(id));
        return ResponseEntity.ok().body(returnMap);
    }
}
/*
Pageable pageable, PagedResourcesAssembler<Member> pagedResourcesAssembler
Page<Member> page = memberRepository.findAll(pageable);
var pagedResources = pagedResourcesAssembler.toResource(page);
return ResponseEntity.ok(pagedResources);*/