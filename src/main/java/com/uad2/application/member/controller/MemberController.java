package com.uad2.application.member.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uad2.application.member.EventValidator;
import com.uad2.application.member.dto.MemberDto;
import com.uad2.application.member.entity.Member;
import com.uad2.application.member.repository.MemberRepository;
import lombok.var;
import org.apache.tomcat.jni.Error;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.springframework.data.domain.Pageable;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

        //(value = "/api")
//
//produces : 응답 형태
@RestController
public class MemberController {
    //public static $AUTO_LOGIN_TIME = 86400*365;
    //insertMember
    //checkId
    //loginCheck
    //checkPwd
    //getMemberFromDB
    //login
    //logout
    //editProfile
    //uploadProfileImage
    static final Logger logger = LoggerFactory.getLogger(MemberController.class);

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    EventValidator eventValidator;

    @GetMapping(value = "/api/member")
    public ResponseEntity getAllMember(Pageable pageable, PagedResourcesAssembler<Member> pagedResourcesAssembler){
        Page<Member> page = memberRepository.findAll(pageable);
        var pagedResources = pagedResourcesAssembler.toResource(page);
        return ResponseEntity.ok(pagedResources);
    }

    @GetMapping(value = "/api/member/{name}")
    public ResponseEntity getMember(@PathVariable String name,Pageable pageable, PagedResourcesAssembler<Member> pagedResourcesAssembler){
        Member member = memberRepository.findByName(name);
        return ResponseEntity.ok(member);
    }

    @PostMapping(value = "/api/member", produces = MediaTypes.HAL_JSON_UTF8_VALUE)
    public ResponseEntity createMember(HttpServletRequest request,
                                       HttpServletResponse response,
                                       @RequestBody @Valid MemberDto memberDto,
                                       Errors errors) throws Exception{
        eventValidator.validate(memberDto,errors);

        if(errors.hasErrors()){
            return ResponseEntity.badRequest().body(errors);
        }
        //폰번호로 존재 유무 체크 필요
        Member savedMember = memberRepository.save(modelMapper.map(memberDto,Member.class));
        URI createdUri = linkTo(MemberController.class).slash(savedMember.getSeq()).toUri();
        return ResponseEntity.created(createdUri).body(savedMember);
    }
}