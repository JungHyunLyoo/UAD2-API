package com.uad2.application.member.controller;

import com.uad2.application.member.MemberValidator;
import com.uad2.application.member.dto.MemberDto;
import com.uad2.application.member.entity.Member;
import com.uad2.application.member.repository.MemberRepository;
import com.uad2.application.member.resource.MemberExternalResource;
import com.uad2.application.member.service.MemberService;
import com.uad2.application.member.resource.MemberResponseUtil;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@RestController
public class MemberController {
    static final Logger logger = LoggerFactory.getLogger(MemberController.class);
    @Autowired//bean등록 된 클래스들을 가져다 쓸 수 있게 함.
    private ModelMapper modelMapper;

    @Autowired
    private MemberRepository memberRepository;

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
        return ResponseEntity.ok(MemberResponseUtil.makeListResponseResource(memberService.getAllMember()));
    }

    /**
     * 회원 id 조회 API
     */
    @GetMapping(value = "/api/member/id/{id}", produces = MediaTypes.HAL_JSON_UTF8_VALUE)
    public ResponseEntity getMemberById(@PathVariable String id) {
        return ResponseEntity.ok(MemberResponseUtil.makeResponseResource(memberService.getMemberById(id)));
    }

    /**
     * 회원 생성 API
     */
    @PostMapping(value = "/api/member", produces = MediaTypes.HAL_JSON_UTF8_VALUE)
    public ResponseEntity createMember(@RequestBody MemberDto.Request requestMember) {
        memberValidator.validateCreateMember(requestMember);
        Member savedMember = memberService.createMember(requestMember);
        URI createdUri = linkTo(MemberController.class).slash("id").slash(requestMember.getId()).toUri();
        MemberExternalResource resource = MemberResponseUtil.makeResponseResource(modelMapper.map(savedMember, Member.class));
        return ResponseEntity.created(createdUri).body(resource);
    }

    /**
     * 비밀번호 체크 - 프로필 수정 검증용
     */
    @PostMapping(value = "/api/member/checkPwd", produces = MediaTypes.HAL_JSON_UTF8_VALUE)
    public ResponseEntity checkPwd(@RequestBody MemberDto.Request requestMember) {
        memberService.checkPwd(requestMember);

        return ResponseEntity.ok().build();
    }

    /**
     * 아이디 중복 체크 - 회원가입시 별도 체크용
     */
    @GetMapping(value = "/api/member/checkId/{id}", produces = MediaTypes.HAL_JSON_UTF8_VALUE)
    public ResponseEntity checkId(@PathVariable String id) {
        memberService.checkId(id);

        return ResponseEntity.ok().build();
    }
}
/*
Pageable pageable, PagedResourcesAssembler<Member> pagedResourcesAssembler
Page<Member> page = memberRepository.findAll(pageable);
var pagedResources = pagedResourcesAssembler.toResource(page);
return ResponseEntity.ok(pagedResources);*/