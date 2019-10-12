package com.uad2.application.member.controller;

import com.uad2.application.common.annotation.Auth;
import com.uad2.application.common.enumData.CookieName;
import com.uad2.application.common.enumData.Role;
import com.uad2.application.exception.ClientException;
import com.uad2.application.member.LoginProcessor;
import com.uad2.application.member.MemberValidator;
import com.uad2.application.member.dto.MemberDto;
import com.uad2.application.member.entity.Member;
import com.uad2.application.member.resource.MemberResponseUtil;
import com.uad2.application.member.service.MemberService;
import com.uad2.application.utils.CookieUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.net.URI;
import java.util.*;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@RestController
public class MemberController {
    static final Logger logger = LoggerFactory.getLogger(MemberController.class);

    @Autowired
    MemberValidator memberValidator;

    @Autowired
    MemberService memberService;

    @Autowired
    LoginProcessor loginProcessor;

    /**
     * 회원 전체 조회 API
     * GetMapping : get 요청을 받아 해당 메소드와 매핑
     * produces : return 하는 형식
     */
    @Auth(role = Role.ADMIN)
    @GetMapping(value = "/api/member", produces = MediaTypes.HAL_JSON_UTF8_VALUE)
    public ResponseEntity getAllMember() {
        List<Member> memberList = Optional.ofNullable(memberService.getAllMember())
                .orElseThrow(() -> new ClientException("Member is not exist"));
        return ResponseEntity.ok(MemberResponseUtil.makeListResponseResource(memberList));
    }

    /**
     * 회원 id 조회 API
     */
    @Auth(role = Role.USER)
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
        if (!ObjectUtils.isEmpty(memberService.findByPhoneNumber(phoneNumber))) {
            throw new ClientException(String.format("PhoneNumber(%s) already exist", phoneNumber));
        }

        Member savedMember = memberService.createMember(requestMember);
        URI createdUri = linkTo(MemberController.class).slash("id").slash(requestMember.getId()).toUri();
        return ResponseEntity.created(createdUri).body(MemberResponseUtil.makeResponseResource(savedMember));
    }

    /**
     * 비밀번호 체크 - 프로필 수정 검증용
     */
    @Auth(role = Role.USER)
    @PostMapping(value = "/api/member/checkPwd", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity checkPwd(@RequestBody MemberDto.Request requestMember) {
        Map<String, Object> returnMap = new HashMap<>();
        returnMap.put("isSamePwd", memberService.isSamePwd(requestMember));
        return ResponseEntity.ok().body(returnMap);
    }

    /**
     * 아이디 중복 체크 - 회원가입시 별도 체크용
     */
    @GetMapping(value = "/api/member/checkId/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity checkId(@PathVariable String id) {
        Map<String,Object> returnMap = new HashMap<>();
        returnMap.put("alreadyExistId",memberService.getMemberById(id) != null);
        return ResponseEntity.ok().body(returnMap);
    }

    /**
     * 로그인 API
     */
    @PostMapping("/api/member/login")
    public ResponseEntity login(
            HttpSession session,
            HttpServletResponse response,
            @RequestBody MemberDto.LoginRequest loginRequest) {
        Map<String,Object> returnMap = new HashMap<>();
        int loginResult = loginProcessor.login(session, response, loginRequest);
        switch (loginResult){
            case LoginProcessor.INVALID_PWD:
                returnMap.put("loginResult","invalid pwd");
                break;
            case LoginProcessor.LOGIN_SUCCESS:
                returnMap.put("loginResult","login success");
                break;
        }
        return ResponseEntity.ok().body(returnMap);
    }

    /**
     * 로그아웃 API
     */
    @GetMapping("/api/member/logout")
    public ResponseEntity logout(HttpServletRequest request, HttpServletResponse response) {
        request.getSession().invalidate();

        // 쿠키 삭제
        Cookie[] cookies = request.getCookies();
        if (Objects.nonNull(cookies)
                && Objects.nonNull(CookieUtil.getCookie(Arrays.asList(cookies), CookieName.SESSION_ID))) {
            for (Cookie cookie : CookieUtil.removeAllCookies(Arrays.asList(request.getCookies()))) {
                response.addCookie(cookie);
            }
        }

        return ResponseEntity.ok().build();
    }

}

/*
Pageable pageable, PagedResourcesAssembler<Member> pagedResourcesAssembler
Page<Member> page = memberRepository.findAll(pageable);
var pagedResources = pagedResourcesAssembler.toResource(page);
return ResponseEntity.ok(pagedResources);*/