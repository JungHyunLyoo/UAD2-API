package com.uad2.application.member.service;

import com.uad2.application.common.TestDescription;
import com.uad2.application.member.dto.MemberDto;
import com.uad2.application.member.entity.Member;
import com.uad2.application.member.repository.MemberRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/**
 * @USER Jongyeob Kim
 * @DATE 2019-11-11
 * @DESCRIPTION {@link MemberService} 테스트
 */
public class MemberServiceTest {

    private MemberService memberService;

    @Mock
    private MemberRepository memberRepository;

    private Member member;
    private List<Member> memberList;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        memberService = new MemberService(memberRepository);

        member = Member.builder()
                .seq(1)
                .id("JunhoPark")
                .pwd("12345")
                .name("Junho")
                .phoneNumber("010-0000-0000")
                .studentId(11)
                .attdCnt(0)
                .isAdmin(1)
                .isWorker(1)
                .isBenefit(1)
                .sessionId("123456789")
                .sessionLimit(new Date())
                .createdAt(LocalDateTime.now())
                .build();

        memberList = Arrays.asList(
                member
        );
    }

    @Test
    @TestDescription("전체 회원 조회 테스트")
    public void getAllMember() {
        given(memberRepository.findAll()).willReturn(memberList);

        List<Member> foundMemberList = memberService.getAllMember();

        assertThat(foundMemberList).isNotNull();

        // 메소드 실행 여부 확인
        verify(memberRepository).findAll();
    }

    @Test
    @TestDescription("회원 개별 조회 by id")
    public void getMember_ById() {
        String id = "JunhoPark";

        given(memberRepository.findById(id)).willReturn(member);

        Member foundMember = memberService.getMemberById(id);

        assertThat(foundMember).isEqualTo(member);

        verify(memberRepository).findById(id);
    }

    @Test
    @TestDescription("회원 개별 조회 by id & sessionId")
    public void getMember_ByIdAndSessionId() {
        String id = "JunhoPark";
        String sessionId = "123456789";

        given(memberRepository.findByIdAndSessionId(id, sessionId)).willReturn(member);

        Member foundMember = memberService.getMemberByIdAndSessionId(id, sessionId);

        assertThat(foundMember).isEqualTo(member);

        verify(memberRepository).findByIdAndSessionId(id, sessionId);
    }

    @Test
    @TestDescription("회원 생성")
    public void create_member() {
        memberService.createMember(member);

        verify(memberRepository).save(any(Member.class));
    }

    @Test
    @TestDescription("회원 세션 정보 수정")
    public void update_sessionInfo() {
        String sessionId = "123456789";
        Date sessionLimit = new Date();

        member.setSessionId(sessionId);
        member.setSessionLimit(sessionLimit);

        memberService.updateSessionInfo(member, sessionId, sessionLimit);

        verify(memberRepository).save(any(Member.class));
    }
/*
    @Test
    @TestDescription("비밀번호 동일 여부 확인")
    public void verify_samePwd() {
        MemberDto.Request requestMember = MemberDto.Request.builder()
                .id(member.getId())
                .pwd(member.getPwd())
                .build();

        given(memberRepository.findById(requestMember.getId())).willReturn(member);

        memberService.isSamePwd(requestMember);

        verify(memberRepository).findById(requestMember.getId());
    }

    @Test(expected = RuntimeException.class)
    @TestDescription("비밀번호 동일 여부 확인 RuntimeException")
    public void verify_samePwd_RuntimeException() {
        MemberDto.Request requestMember = MemberDto.Request.builder()
                .id(member.getId())
                .pwd(member.getPwd())
                .build();

        given(memberRepository.findById(requestMember.getId())).willThrow(new RuntimeException());

        memberService.isSamePwd(requestMember);

        verify(memberRepository).findById(requestMember.getId());
    }

*/
    @Test
    @TestDescription("회원 개별 조회 by phoneNumber")
    public void getMember_ByPhoneNumber() {
        given(memberRepository.findByPhoneNumber(member.getPhoneNumber())).willReturn(member);

        memberService.findByPhoneNumber(member.getPhoneNumber());

        verify(memberRepository).findByPhoneNumber(member.getPhoneNumber());
    }

    @Test
    @TestDescription("전체 회원 조회 by seq list")
    public void getAllMember_BySeqList() {
        int[] seqList = new int[]{1, 2, 3};
        given(memberRepository.findAllBySeqIn(seqList)).willReturn(memberList);

        memberService.findBySeqList(seqList);

        verify(memberRepository).findAllBySeqIn(seqList);
    }

}