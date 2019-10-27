package com.uad2.application.calculation.controller;
/*
 * @USER JungHyun
 * @DATE 2019-10-27
 * @DESCRIPTION
 */

import com.uad2.application.common.BaseControllerTest;
import com.uad2.application.common.TestDescription;
import com.uad2.application.common.enumData.CookieName;
import com.uad2.application.member.entity.Member;
import org.junit.Before;
import org.junit.Test;
import org.springframework.hateoas.MediaTypes;
import org.springframework.mock.web.MockCookie;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.ResultActions;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.TimeZone;

import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.subsectionWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CalculationControllerTests  extends BaseControllerTest {

    private MockHttpSession session;
    private Member adminMember;
    private Member userMember;

    @Before
    public void setUp() {
        final int expiredTime = 60 * 60 * 24;
        session = new MockHttpSession();
        session.setMaxInactiveInterval(expiredTime * 365);

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.add(Calendar.SECOND, expiredTime);
        adminMember = Member.builder()
                .seq(1)
                .id("testAdmin")
                .pwd("testAdmin")
                .name("testAdmin")
                .phoneNumber("01000000001")
                .sessionId(null)
                .sessionLimit(calendar.getTime())
                .isAdmin(1)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        userMember = Member.builder()
                .seq(135)
                .id("testUser")
                .pwd("testUser")
                .name("testUser")
                .phoneNumber("01000000000")
                .sessionId("1B3F16603664677C2ABE377C71ABF196")
                .sessionLimit(calendar.getTime())
                .isAdmin(0)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Test
    @Transactional
    @TestDescription("전체 정산 데이터 내역 조회")
    public void getAllCalculations() throws Exception {

        MockCookie id = new MockCookie(CookieName.ID.getName(), userMember.getId());
        MockCookie name = new MockCookie(CookieName.NAME.getName(), userMember.getName());
        MockCookie phoneNum = new MockCookie(CookieName.PHONE_NUM.getName(), userMember.getPhoneNumber());
        MockCookie isWorker = new MockCookie(CookieName.IS_WORKER.getName(), Integer.toString(userMember.getIsWorker()));
        MockCookie sessionId = new MockCookie(CookieName.SESSION_ID.getName(), userMember.getSessionId());
        MockCookie isAdmin = new MockCookie(CookieName.IS_ADMIN.getName(), Integer.toString(userMember.getIsAdmin()));
        MockCookie isAutoLogin = new MockCookie(CookieName.IS_AUTO_LOGIN.getName(), "false");

        // request
        ResultActions result = mockMvc.perform(
                RestDocumentationRequestBuilders.get("/api/calculation/year/{year}/month/{month}/memberSeq/{memberSeq}",2019,10,135)
                        .cookie(id)
                        .cookie(name)
                        .cookie(phoneNum)
                        .cookie(isWorker)
                        .cookie(sessionId)
                        .cookie(isAdmin)
                        .cookie(isAutoLogin)
        );
        // result
        result.andExpect(status().isOk())
                .andDo(print());
    }
/*
    @Test
    @Transactional
    @TestDescription("전체 정산 데이터 내역 조회 에러(로그인 x)")
    public void getAllCalculations_badRequest_noAuth() throws Exception {
    }

    @Test
    @Transactional
    @TestDescription("전체 정산 데이터 내역 조회 에러(파라미터 부재)")
    public void getAllCalculations_badRequest_noPathVariable() throws Exception {
    }

    @Test
    @Transactional
    @TestDescription("전체 정산 데이터 내역 조회 에러(다른 회원의 내역 조회)")
    public void getAllCalculations_badRequest_requestOtherMemberInfo() throws Exception {
    }*/
}
