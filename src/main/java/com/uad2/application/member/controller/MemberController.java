package com.uad2.application.member.controller;

import com.uad2.application.member.MemberValidator;
import com.uad2.application.member.dto.MemberDto;
import com.uad2.application.member.entity.Member;
import com.uad2.application.member.repository.MemberRepository;
import com.uad2.application.member.service.MemberService;
import com.uad2.application.utils.MemberResponseUtil;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@RestController
public class MemberController {
    static final Logger logger = LoggerFactory.getLogger(MemberController.class);
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    MemberValidator memberValidator;

    @Autowired
    MemberService memberService;

    /**
     * 회원 전체 조회 API
     */
    @GetMapping(value = "/api/member", produces = MediaTypes.HAL_JSON_UTF8_VALUE)
    public ResponseEntity getAllMember() {
        List<Member> memberList = memberRepository.findAll();
        return ResponseEntity.ok(MemberResponseUtil.makeListResponseResource(memberList));
    }

    /**
     * 회원 id 조회 API
     */
    @GetMapping(value = "/api/member/id/{id}", produces = MediaTypes.HAL_JSON_UTF8_VALUE)
    public ResponseEntity getMemberById(@PathVariable String id) {
        Member member = memberRepository.findById(id);
        return ResponseEntity.ok(MemberResponseUtil.makeResponseResource(member));
    }

    /**
     * 회원 생성 API
     */
    @PostMapping(value = "/api/member", produces = MediaTypes.HAL_JSON_UTF8_VALUE)
    public ResponseEntity createMember(@RequestBody MemberDto.Request requestMember, Errors errors) throws Exception {
        memberValidator.createMemberValidate(requestMember, errors);
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(errors);
        }
        Member savedMember = memberService.createMember(requestMember);
        if (savedMember != null) {
            URI createdUri = linkTo(MemberController.class).slash("id").slash(requestMember.getId()).toUri();
            return ResponseEntity.created(createdUri).body(MemberResponseUtil.makeResponseResource(modelMapper.map(savedMember, Member.class)));
        } else {
            return ResponseEntity.status(202).build();
        }
    }
}
/*
Pageable pageable, PagedResourcesAssembler<Member> pagedResourcesAssembler
Page<Member> page = memberRepository.findAll(pageable);
var pagedResources = pagedResourcesAssembler.toResource(page);
return ResponseEntity.ok(pagedResources);*/