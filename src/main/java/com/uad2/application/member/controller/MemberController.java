package com.uad2.application.member.controller;

import com.uad2.application.common.annotation.Auth;
import com.uad2.application.common.enumData.CookieName;
import com.uad2.application.common.enumData.Role;
import com.uad2.application.config.PropertiesBundle;
import com.uad2.application.exception.ClientException;
import com.uad2.application.member.LoginProcessor;
import com.uad2.application.member.MemberValidator;
import com.uad2.application.member.dto.MemberDto;
import com.uad2.application.member.entity.Member;
import com.uad2.application.member.resource.MemberResponseUtil;
import com.uad2.application.member.service.MemberService;
import com.uad2.application.utils.CookieUtil;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.net.URI;
import java.util.*;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@RestController
public class MemberController {
    static final Logger logger = LoggerFactory.getLogger(MemberController.class);

    private final MemberValidator memberValidator;
    private final MemberService memberService;
    private final LoginProcessor loginProcessor;
    private final ModelMapper modelMapper;

    @Autowired
    public MemberController(MemberValidator memberValidator,
                            MemberService memberService,
                            LoginProcessor loginProcessor,
                            ModelMapper modelMapper) {
        this.memberValidator = memberValidator;
        this.memberService = memberService;
        this.loginProcessor = loginProcessor;
        this.modelMapper = modelMapper;
    }


    @GetMapping(value = "/")
    public ResponseEntity index() {
        return ResponseEntity.ok().body("index");
    }


    /**
     * 회원 전체 조회 API
     * GetMapping : get 요청을 받아 해당 메소드와 매핑
     * produces : return 하는 형식
     */
    @Auth(role = Role.ADMIN)
    @GetMapping(value = "/api/member", produces = MediaTypes.HAL_JSON_UTF8_VALUE)
    public ResponseEntity getAllMember() {
        List<Member> memberList = memberService.getAllMember();
        return ResponseEntity.ok(MemberResponseUtil.makeListResponseResource(memberList));
    }

    /**
     * 회원 id 조회 API
     */
    @Auth(role = Role.USER)
    @GetMapping(value = "/api/member/id/{id}", produces = MediaTypes.HAL_JSON_UTF8_VALUE)
    public ResponseEntity getMemberById(@PathVariable String id) {
        Member member = memberService.getMemberById(id);
        return ResponseEntity.ok(MemberResponseUtil.makeResponseResource(member));
    }

    /**
     * 회원 생성 API
     */
    @PostMapping(value = "/api/member", produces = MediaTypes.HAL_JSON_UTF8_VALUE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity createMember(MemberDto.Request requestMember,
                                       @RequestParam(value = "profileImg") MultipartFile profileImg) throws Exception {

        //파라미터 체크
        memberValidator.validateCreateMember(requestMember);

        //폰번호로 유일성 체크
        String phoneNumber = requestMember.getPhoneNumber();
        if (!ObjectUtils.isEmpty(memberService.findByPhoneNumber(phoneNumber))) {
            throw new ClientException(String.format("PhoneNumber(%s) already exist", phoneNumber));
        }

        //프로필 이미지 저장
        profileImg.transferTo(new File(PropertiesBundle.PROFILE_IMAGE_DIRECTORY + requestMember.getName() + "_profile.png"));

        Member savedMember = memberService.createMember(modelMapper.map(requestMember, Member.class));
        URI createdUri = linkTo(MemberController.class).slash("id").slash(requestMember.getId()).toUri();
        return ResponseEntity.created(createdUri).body(MemberResponseUtil.makeResponseResource(savedMember));
    }

    /**
     * 비밀번호 체크 - 프로필 수정 검증용
     */
    @Auth(role = Role.USER)
    @PostMapping(value = "/api/member/checkPwd", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity checkPwd(@RequestBody MemberDto.Request requestMember) {
        memberValidator.validateCheckPwd(requestMember);
        Map<String, Object> returnMap = new HashMap<>();
        returnMap.put("isSamePwd", memberService.isSamePwd(requestMember));
        return ResponseEntity.ok().body(returnMap);
    }

    /**
     * 로그인 API
     */
    @PostMapping("/api/member/login")
    public ResponseEntity login(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestBody MemberDto.LoginRequest loginRequest) {
        loginProcessor.login(request, response, loginRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/api/member/autoLogin")
    public ResponseEntity autoLogin(
            HttpServletRequest request,
            HttpServletResponse response) {
        loginProcessor.login(request, response, null);
        return ResponseEntity.ok().build();
    }

    /**
     * 로그아웃 API
     */
    @GetMapping("/api/member/logout")
    public ResponseEntity logout(
            HttpSession session,
            HttpServletRequest request,
            HttpServletResponse response) {
        loginProcessor.logout(request, response, session);
        return ResponseEntity.ok().build();
    }

    /**
     * 자동 로그인 체크
     */
    @GetMapping(value = "/api/member/checkAutoLogin", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity checkAutoLogin(
            HttpServletRequest request) {
        //쿠키 획득
        List<Cookie> cookieList = Optional.ofNullable(request.getCookies())
                .map(Arrays::asList)
                .orElseThrow(() -> new ClientException("Cookies are not exist"));

        logger.debug("cookie list = {}", cookieList);

        boolean isAutoLogin = Boolean.parseBoolean(
                Optional.ofNullable(CookieUtil.getCookie(cookieList, CookieName.IS_AUTO_LOGIN).getValue())
                        .orElse("false")
        );

        if (isAutoLogin) {
            //자동로그인 체크
            loginProcessor.checkAutoLogin(cookieList);
        }

        Map<String, Object> returnMap = new HashMap<>();
        returnMap.put("isAutoLogin", isAutoLogin);

        return ResponseEntity.ok().body(returnMap);
    }

}

/*
Pageable pageable, PagedResourcesAssembler<Member> pagedResourcesAssembler
Page<Member> page = memberRepository.findAll(pageable);
var pagedResources = pagedResourcesAssembler.toResource(page);
return ResponseEntity.ok(pagedResources);*/