package com.uad2.application.member.controller;

import com.uad2.application.member.MemberValidator;
import com.uad2.application.member.entity.Member;
import com.uad2.application.member.entity.MemberInsertDto;
import com.uad2.application.member.repository.MemberRepository;
import com.uad2.application.utils.MemberResponseUtil;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@RestController
public class MemberController {
    static final Logger logger = LoggerFactory.getLogger(MemberController.class);

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    MemberValidator memberValidator;

    @GetMapping(value = "/api/member", produces = MediaTypes.HAL_JSON_UTF8_VALUE)
    public ResponseEntity getAllMember(){
        List<Member> memberList = memberRepository.findAll();
        return ResponseEntity.ok(MemberResponseUtil.makeListResponseResource(memberList));
    }
    @GetMapping(value = "/api/member/id/{id}", produces = MediaTypes.HAL_JSON_UTF8_VALUE)
    public ResponseEntity getMemberById(@PathVariable String id){
        Member member = memberRepository.findById(id);
        return ResponseEntity.ok(MemberResponseUtil.makeResponseResource(member));
    }
    @PostMapping(value = "/api/member", produces = MediaTypes.HAL_JSON_UTF8_VALUE)
    public ResponseEntity createMember(@RequestBody MemberInsertDto memberInsertDto, Errors errors) throws Exception{
        memberValidator.createMemberValidate(memberInsertDto,errors);
        if(errors.hasErrors()){
            return ResponseEntity.badRequest().body(errors);
        }
        String phoneNumber = memberInsertDto.getPhoneNumber();
        Member member = memberRepository.findByPhoneNumber(phoneNumber);
        if(member == null){
            Member savedMember = memberRepository.save(modelMapper.map(memberInsertDto,Member.class));
            URI createdUri = linkTo(MemberController.class).slash("id").slash(savedMember.getId()).toUri();
            return ResponseEntity.created(createdUri).body(MemberResponseUtil.makeResponseResource(savedMember));
        }
        else{
            return ResponseEntity.status(202).build();
        }
    }
}
/*
Pageable pageable, PagedResourcesAssembler<Member> pagedResourcesAssembler
Page<Member> page = memberRepository.findAll(pageable);
var pagedResources = pagedResourcesAssembler.toResource(page);
return ResponseEntity.ok(pagedResources);*/