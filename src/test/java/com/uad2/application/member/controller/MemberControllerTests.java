package com.uad2.application.member.controller;

/*
 * @USER JungHyun
 * @DATE 2019-09-09
 */

import com.uad2.application.common.BaseControllerTest;
import com.uad2.application.common.TestDescription;
import com.uad2.application.member.dto.LoginDto;
import com.uad2.application.member.entity.Member;
import com.uad2.application.member.entity.MemberInsertDto;
import com.uad2.application.utils.CookieUtil;
import org.junit.Before;
import org.junit.Test;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockCookie;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


//@RunWith(JUnitParamsRunner.class)
public class MemberControllerTests extends BaseControllerTest {

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
                .sessionId(session.getId())
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
                .sessionId(session.getId())
                .sessionLimit(calendar.getTime())
                .isAdmin(0)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Test
    @Transactional
    @TestDescription("전체 회원 조회")
    public void getAllMembers() throws Exception {
        session.setAttribute("member", adminMember);

        // request
        ResultActions result = mockMvc.perform(
                RestDocumentationRequestBuilders.get("/api/member")
                        .session(session)
                        .accept(MediaTypes.HAL_JSON)
        );

        // result
        result.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("memberList").exists())
                .andDo(document("getAllMembers",//조각 모음 폴더의 이름
                        links(
                                linkWithRel("profile").description("restDoc link")
                        ),
                        responseFields(
                                subsectionWithPath("memberList").description("전체 회원 데이터 리스트"),
                                subsectionWithPath("_links").description("링크")
                        )
                ));
    }

    @Test
    @Transactional
    @TestDescription("멤버 개별 조회 by id")
    public void getMemberById() throws Exception {
        session.setAttribute("member", userMember);
        // request
        ResultActions result = mockMvc.perform(
                RestDocumentationRequestBuilders.get("/api/member/id/{id}", "dkcmsa")
                        .session(session)
                        .accept(MediaTypes.HAL_JSON)
        );

        // result
        result.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("member").exists())
                .andDo(document("getMemberById",
                        pathParameters(
                                parameterWithName("id").description("아이디")
                        ),
                        links(
                                linkWithRel("profile").description("restDoc link")
                        ),
                        responseFields(
                                subsectionWithPath("member").description("회원 데이터"),
                                subsectionWithPath("_links").description("링크")
                        )
                ));
    }

    @Test
    @Transactional
    @TestDescription("개별 회원 조회 에러(매개변수 미기입)")
    public void getMemberById_badRequest_emptyParameter() throws Exception {
        session.setAttribute("member", userMember);

        // request
        ResultActions result = mockMvc.perform(
                RestDocumentationRequestBuilders.get("/api/member/id/{id}", "")
                        .session(session)
                        .accept(MediaTypes.HAL_JSON)
        );

        // result
        result.andExpect(status().isNotFound())
                .andDo(print())
                .andDo(document("getMemberById_badRequest_emptyParameter",
                        pathParameters(
                                parameterWithName("id").description("아이디")
                        )));
    }

    @Test
    @Transactional
    @TestDescription("회원 데이터 생성")
    public void createMember() throws Exception {
        MemberInsertDto member = MemberInsertDto.builder()
                .id("test")
                .pwd("test")
                .name("test")
                .birthDay(new Date())
                .studentId(11)
                .isWorker(0)
                .phoneNumber("01234567890")
                .build();

        // request
        ResultActions result = mockMvc.perform(
                RestDocumentationRequestBuilders.post("/api/member")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaTypes.HAL_JSON)
                        .content(objectMapper.writeValueAsString(member))
        );

        // result
        result.andExpect(status().isCreated())
                .andDo(print())
                .andExpect(jsonPath("member").exists())
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_UTF8_VALUE))
                .andDo(document("createMember",
                        requestFields(
                                fieldWithPath("id").type(JsonFieldType.STRING).description("아이디"),
                                fieldWithPath("pwd").type(JsonFieldType.STRING).description("비밀번호"),
                                fieldWithPath("name").type(JsonFieldType.STRING).description("이름"),
                                fieldWithPath("birthDay").type(JsonFieldType.STRING).description("생일"),
                                fieldWithPath("studentId").type(JsonFieldType.NUMBER).description("학번"),
                                fieldWithPath("isWorker").type(JsonFieldType.NUMBER).description("직장인 여부"),
                                fieldWithPath("phoneNumber").type(JsonFieldType.STRING).description("핸드폰 번호")
                        ),
                        responseFields(
                                subsectionWithPath("member").description("회원 데이터"),
                                subsectionWithPath("_links").description("링크")
                        )
                ));
    }

    @Test
    public void createMember_badRequest() throws Exception {
        /*
        Member member = Member.builder()
                .id("testId")
                .pwd("testPwd")
                .name("testName").build();

        mockMvc.perform(post("/api/member")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(member)))
                .andDo(print())
                .andExpect(status().isBadRequest());*/
    }

    @Test
    public void createMember_badRequest_emptyInput() throws Exception {/*
        MemberExternalDto memberExternalDto = MemberExternalDto.builder().build();

        mockMvc.perform(post("/api/member")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsBytes(memberExternalDto)))
                .andDo(print())
                .andExpect(status().isBadRequest());*/
    }

    @Test
    public void createMember_badRequest_wrongInput() throws Exception {/*
        MemberExternalDto memberExternalDto = MemberExternalDto.builder().build();

        mockMvc.perform(post("/api/member")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsBytes(memberExternalDto))
        ).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].objectName").exists());*/
    }

    @Test
    @Transactional
    @TestDescription("일반 로그인 테스트 (자동 로그인 true)")
    public void loginMember_general_isAutoLogin_true() throws Exception {
        LoginDto login = LoginDto.builder()
                .id("testUser")
                .pwd("testUser")
                .isAutoLogin(true)
                .build();

        // request
        ResultActions result = mockMvc.perform(
                RestDocumentationRequestBuilders.post("/api/member/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(login))
        );

        result.andExpect(status().isOk())
                .andDo(print())
                .andExpect(cookie().exists(CookieUtil.CookieName.ID.getName()))
                .andExpect(cookie().exists(CookieUtil.CookieName.NAME.getName()))
                .andExpect(cookie().exists(CookieUtil.CookieName.PHONE_NUM.getName()))
                .andExpect(cookie().exists(CookieUtil.CookieName.IS_WORKER.getName()))
                .andExpect(cookie().exists(CookieUtil.CookieName.SESSION_ID.getName()))
                .andExpect(cookie().exists(CookieUtil.CookieName.IS_ADMIN.getName()))
                .andExpect(cookie().exists(CookieUtil.CookieName.IS_AUTO_LOGIN.getName()));
    }

    @Test
    @Transactional
    @TestDescription("일반 로그인 테스트 (자동 로그인 false)")
    public void loginMember_general_isAutoLogin_false() throws Exception {
        LoginDto login = LoginDto.builder()
                .id("testUser")
                .pwd("testUser")
                .isAutoLogin(false)
                .build();

        // request
        ResultActions result = mockMvc.perform(
                RestDocumentationRequestBuilders.post("/api/member/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(login))
        );

        result.andExpect(status().isOk())
                .andDo(print())
                .andExpect(cookie().doesNotExist(CookieUtil.CookieName.ID.getName()))
                .andExpect(cookie().doesNotExist(CookieUtil.CookieName.NAME.getName()))
                .andExpect(cookie().doesNotExist(CookieUtil.CookieName.PHONE_NUM.getName()))
                .andExpect(cookie().doesNotExist(CookieUtil.CookieName.IS_WORKER.getName()))
                .andExpect(cookie().doesNotExist(CookieUtil.CookieName.SESSION_ID.getName()))
                .andExpect(cookie().doesNotExist(CookieUtil.CookieName.IS_ADMIN.getName()))
                .andExpect(cookie().doesNotExist(CookieUtil.CookieName.IS_AUTO_LOGIN.getName()));
    }

    @Test
    @Transactional
    @TestDescription("자동 로그인 테스트")
    public void loginMember_auto() throws Exception {
        MockCookie id = new MockCookie(CookieUtil.CookieName.ID.getName(), userMember.getId());
        MockCookie name = new MockCookie(CookieUtil.CookieName.NAME.getName(), userMember.getName());
        MockCookie phoneNum = new MockCookie(CookieUtil.CookieName.PHONE_NUM.getName(), userMember.getPhoneNumber());
        MockCookie isWorker = new MockCookie(CookieUtil.CookieName.IS_WORKER.getName(), Integer.toString(userMember.getIsWorker()));
        MockCookie sessionId = new MockCookie(CookieUtil.CookieName.SESSION_ID.getName(), Integer.toString(1));
        MockCookie isAdmin = new MockCookie(CookieUtil.CookieName.IS_ADMIN.getName(), Integer.toString(userMember.getIsAdmin()));
        MockCookie isAutoLogin = new MockCookie(CookieUtil.CookieName.IS_AUTO_LOGIN.getName(), Boolean.toString(true));

        ResultActions result = mockMvc.perform(
                RestDocumentationRequestBuilders.post("/api/member/login")
                        .cookie(id)
                        .cookie(name)
                        .cookie(phoneNum)
                        .cookie(isWorker)
                        .cookie(sessionId)
                        .cookie(isAdmin)
                        .cookie(isAutoLogin)
        );

        result.andExpect(status().isOk())
                .andDo(print())
                .andExpect(cookie().exists(CookieUtil.CookieName.ID.getName()))
                .andExpect(cookie().exists(CookieUtil.CookieName.NAME.getName()))
                .andExpect(cookie().exists(CookieUtil.CookieName.PHONE_NUM.getName()))
                .andExpect(cookie().exists(CookieUtil.CookieName.IS_WORKER.getName()))
                .andExpect(cookie().exists(CookieUtil.CookieName.SESSION_ID.getName()))
                .andExpect(cookie().exists(CookieUtil.CookieName.IS_ADMIN.getName()))
                .andExpect(cookie().exists(CookieUtil.CookieName.IS_AUTO_LOGIN.getName()));
    }

    /*
    private Object[] paramsForTestFree(){
        return new Object[]{
                new Object[] {0},
                new Object[] {0}
        };
    }
    @Test
    @Parameters(method = "paramsForTestFree")
    public void testTest(int n1){
        Assert.assertEquals(n1,0);
    }*/
}
