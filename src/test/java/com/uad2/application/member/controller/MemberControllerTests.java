package com.uad2.application.member.controller;

/*
 * @USER JungHyun
 * @DATE 2019-09-09
 */

import com.uad2.application.BaseControllerTest;
import com.uad2.application.common.enumData.CookieName;
import com.uad2.application.common.TestDescription;
import com.uad2.application.member.dto.MemberDto;
import com.uad2.application.utils.CookieUtil;
import org.junit.Test;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockCookie;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import javax.transaction.Transactional;
import java.util.*;

import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class MemberControllerTests extends BaseControllerTest {
    @Test
    @Transactional
    @TestDescription("전체 회원 조회")
    public void getAllMembers() throws Exception {
        MockCookie[] adminMemberCookieList = super.getAdminMemberCookieList(AUTOLOGIN_FALSE);
        // request
        ResultActions result = mockMvc.perform(
                super.getRequest("/api/member", adminMemberCookieList)
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
    @TestDescription("전체 회원 조회 에러(일반 유저 로그인)")
    public void getAllMembers_badRequest_noAuth() throws Exception {
        MockCookie[] userMemberCookieList = super.getUserMemberCookieList(AUTOLOGIN_FALSE);
        // request
        ResultActions result = mockMvc.perform(
                super.getRequest("/api/member", userMemberCookieList)
                        .accept(MediaTypes.HAL_JSON)
        );
        // result
        result.andExpect(status().isAccepted())
                .andDo(print());
    }
    @Test
    @Transactional
    @TestDescription("전체 회원 조회 에러(로그인 x)")
    public void getAllMembers_badRequest_noLogin() throws Exception {
        // request
        ResultActions result = mockMvc.perform(
                super.getRequest("/api/member")
                        .accept(MediaTypes.HAL_JSON)
        );
        // result
        result.andExpect(status().isAccepted())
                .andDo(print());
    }
    @Test
    @Transactional
    @TestDescription("멤버 개별 조회 by id")
    public void getMemberById() throws Exception {
        String[] paramList = new String[]{"testUser"};
        MockCookie[] userMemberCookieList = super.getMemberCookieList(userMember, AUTOLOGIN_TRUE);
        // request
        ResultActions result = mockMvc.perform(
                super.getRequest("/api/member/id/{id}",paramList, userMemberCookieList)
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
    @TestDescription("멤버 개별 조회(해당 아이디로 데이터 x) by id")
    public void getMemberById_emptyResult() throws Exception {
        String[] paramList = new String[]{"testUser123"};
        MockCookie[] userMemberCookieList = super.getMemberCookieList(userMember, AUTOLOGIN_TRUE);
        // request
        ResultActions result = mockMvc.perform(
                super.getRequest("/api/member/id/{id}",paramList, userMemberCookieList)
                        .accept(MediaTypes.HAL_JSON)
        );
        // result
        result.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("member").doesNotExist())
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
    @TestDescription("멤버 개별 조회 by id 에러(로그인 x)")
    public void getMemberById_badRequest_noLogin() throws Exception {
        String[] paramList = new String[]{"testUser"};
        // request
        ResultActions result = mockMvc.perform(
                super.getRequest("/api/member/id/{id}",paramList)
                        .accept(MediaTypes.HAL_JSON)
        );

        // result
        result.andExpect(status().isAccepted())
                .andDo(print());
    }
    @Test
    @Transactional
    @TestDescription("개별 회원 조회 에러(매개변수 미기입)")
    public void getMemberById_badRequest_emptyParameter() throws Exception {
        String[] paramList = new String[]{""};
        // request
        ResultActions result = mockMvc.perform(
                super.getRequest("/api/member/id/{id}",paramList)
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
        MemberDto.Request member = MemberDto.Request.builder()
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
                super.postRequest("/api/member",member)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaTypes.HAL_JSON)
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
                                fieldWithPath("phoneNumber").type(JsonFieldType.STRING).description("핸드폰 번호"),
                                fieldWithPath("isAutoLogin").type(JsonFieldType.BOOLEAN).description("자동 로그인 flag").optional()
                        ),
                        responseFields(
                                subsectionWithPath("member").description("회원 데이터"),
                                subsectionWithPath("_links").description("링크")
                        )
                ));
    }
    @Test
    @Transactional
    @TestDescription("회원 데이터 생성 에러(필수 파라미터 부재)")
    public void createMember_badRequest_emptyRequest() throws Exception {
        MemberDto.Request member = MemberDto.Request.builder()
                .pwd("test")
                .name("test")
                .birthDay(new Date())
                .studentId(11)
                .isWorker(0)
                .phoneNumber("01234567890")
                .build();
        // request
        ResultActions result = mockMvc.perform(
                super.postRequest("/api/member",member)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaTypes.HAL_JSON)
        );
        // result
        result.andExpect(status().isAccepted())
                .andDo(print())
                .andExpect(jsonPath("message").exists())
                .andExpect(jsonPath("message").value("id is empty"));
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
    @TestDescription("일반 로그인 (자동 로그인 false)")
    public void loginMember_general_isAutoLogin_false() throws Exception {
        MemberDto.LoginRequest loginRequest = MemberDto.LoginRequest.builder()
                .id("testUser")
                .pwd("testUser")
                .isAutoLogin(false)
                .build();
        // request
        ResultActions result = mockMvc.perform(
                super.postRequest("/api/member/login",loginRequest)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaTypes.HAL_JSON)
        );
        // result
        result.andExpect(status().isOk())
                .andDo(print())
                .andExpect(cookie().exists(CookieName.ID.getName()))
                .andExpect(cookie().exists(CookieName.NAME.getName()))
                .andExpect(cookie().exists(CookieName.PHONE_NUM.getName()))
                .andExpect(cookie().exists(CookieName.IS_WORKER.getName()))
                .andExpect(cookie().exists(CookieName.SESSION_ID.getName()))
                .andExpect(cookie().exists(CookieName.IS_ADMIN.getName()));
    }
    @Test
    @Transactional
    @TestDescription("일반 로그인 아이디 오류(자동 로그인 false)")
    public void loginMember_general_isAutoLogin_false_badRequest_invalidId() throws Exception {
        MemberDto.LoginRequest loginRequest = MemberDto.LoginRequest.builder()
                .id("testUser123")
                .pwd("testUser")
                .isAutoLogin(false)
                .build();
        // request
        ResultActions result = mockMvc.perform(
                super.postRequest("/api/member/login",loginRequest)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaTypes.HAL_JSON)
        );
        // result
        result.andExpect(status().isAccepted())
                .andDo(print())
                .andExpect(jsonPath("message").exists())
                .andExpect(jsonPath("message").value("Id is not exist"))
                .andExpect(cookie().doesNotExist(CookieName.ID.getName()))
                .andExpect(cookie().doesNotExist(CookieName.NAME.getName()))
                .andExpect(cookie().doesNotExist(CookieName.PHONE_NUM.getName()))
                .andExpect(cookie().doesNotExist(CookieName.IS_WORKER.getName()))
                .andExpect(cookie().doesNotExist(CookieName.SESSION_ID.getName()))
                .andExpect(cookie().doesNotExist(CookieName.IS_ADMIN.getName()));
    }
    @Test
    @Transactional
    @TestDescription("일반 로그인 비밀번호 오류(자동 로그인 false)")
    public void loginMember_general_isAutoLogin_false_badRequest_invalidPwd() throws Exception {
        MemberDto.LoginRequest loginRequest = MemberDto.LoginRequest.builder()
                .id("testUser")
                .pwd("wrongPwd")
                .isAutoLogin(false)
                .build();
        // request
        ResultActions result = mockMvc.perform(
                super.postRequest("/api/member/login",loginRequest)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaTypes.HAL_JSON)
        );
        // result
        result.andExpect(status().isAccepted())
                .andDo(print())
                .andExpect(jsonPath("message").exists())
                .andExpect(jsonPath("message").value("Pwd is not correct"))
                .andExpect(cookie().doesNotExist(CookieName.ID.getName()))
                .andExpect(cookie().doesNotExist(CookieName.NAME.getName()))
                .andExpect(cookie().doesNotExist(CookieName.PHONE_NUM.getName()))
                .andExpect(cookie().doesNotExist(CookieName.IS_WORKER.getName()))
                .andExpect(cookie().doesNotExist(CookieName.SESSION_ID.getName()))
                .andExpect(cookie().doesNotExist(CookieName.IS_ADMIN.getName()));
    }
    @Test
    @Transactional
    @TestDescription("자동 로그인")
    public void loginMember_autoLogin_true() throws Exception {
        MemberDto.LoginRequest loginRequest = MemberDto.LoginRequest.builder()
                .id("testUser")
                .pwd("testUser")
                .isAutoLogin(true)
                .build();
        // request
        ResultActions result = mockMvc.perform(
                super.postRequest("/api/member/login",loginRequest)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaTypes.HAL_JSON)
        );
        // result
        result.andExpect(status().isOk())
                .andDo(print())
                .andExpect(cookie().exists(CookieName.ID.getName()))
                .andExpect(cookie().exists(CookieName.NAME.getName()))
                .andExpect(cookie().exists(CookieName.PHONE_NUM.getName()))
                .andExpect(cookie().exists(CookieName.IS_WORKER.getName()))
                .andExpect(cookie().exists(CookieName.SESSION_ID.getName()))
                .andExpect(cookie().exists(CookieName.IS_ADMIN.getName()))
                .andExpect(cookie().maxAge(CookieName.ID.getName(), CookieUtil.A_YEAR_EXPIRATION))
                .andExpect(cookie().maxAge(CookieName.NAME.getName(), CookieUtil.A_YEAR_EXPIRATION))
                .andExpect(cookie().maxAge(CookieName.PHONE_NUM.getName(), CookieUtil.A_YEAR_EXPIRATION))
                .andExpect(cookie().maxAge(CookieName.IS_WORKER.getName(), CookieUtil.A_YEAR_EXPIRATION))
                .andExpect(cookie().maxAge(CookieName.SESSION_ID.getName(), CookieUtil.A_YEAR_EXPIRATION))
                .andExpect(cookie().maxAge(CookieName.IS_ADMIN.getName(), CookieUtil.A_YEAR_EXPIRATION));
    }
    @Test
    @Transactional
    @TestDescription("자동 로그인 아이디 오류")
    public void loginMember_autoLogin_true_badRequest_invalidId() throws Exception {
        MemberDto.LoginRequest loginRequest = MemberDto.LoginRequest.builder()
                .id("invalidId")
                .pwd("testUser")
                .isAutoLogin(true)
                .build();
        // request
        ResultActions result = mockMvc.perform(
                super.postRequest("/api/member/login",loginRequest)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaTypes.HAL_JSON)
        );
        // result
        result.andExpect(status().isAccepted())
                .andDo(print())
                .andExpect(jsonPath("message").exists())
                .andExpect(jsonPath("message").value("Id is not exist"))
                .andExpect(cookie().doesNotExist(CookieName.ID.getName()))
                .andExpect(cookie().doesNotExist(CookieName.NAME.getName()))
                .andExpect(cookie().doesNotExist(CookieName.PHONE_NUM.getName()))
                .andExpect(cookie().doesNotExist(CookieName.IS_WORKER.getName()))
                .andExpect(cookie().doesNotExist(CookieName.SESSION_ID.getName()))
                .andExpect(cookie().doesNotExist(CookieName.IS_ADMIN.getName()));
    }
    @Test
    @Transactional
    @TestDescription("자동 로그인 비밀번호 오류")
    public void loginMember_autoLogin_true_badRequest_invalidPwd() throws Exception {
        MemberDto.LoginRequest loginRequest = MemberDto.LoginRequest.builder()
                .id("testUser")
                .pwd("invalidPwd")
                .isAutoLogin(true)
                .build();
        // request
        ResultActions result = mockMvc.perform(
                super.postRequest("/api/member/login",loginRequest)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaTypes.HAL_JSON)
        );
        // result
        result.andExpect(status().isAccepted())
                .andDo(print())
                .andExpect(jsonPath("message").exists())
                .andExpect(jsonPath("message").value("Pwd is not correct"))
                .andExpect(cookie().doesNotExist(CookieName.ID.getName()))
                .andExpect(cookie().doesNotExist(CookieName.NAME.getName()))
                .andExpect(cookie().doesNotExist(CookieName.PHONE_NUM.getName()))
                .andExpect(cookie().doesNotExist(CookieName.IS_WORKER.getName()))
                .andExpect(cookie().doesNotExist(CookieName.SESSION_ID.getName()))
                .andExpect(cookie().doesNotExist(CookieName.IS_ADMIN.getName()));
    }
    @Test
    @Transactional
    @TestDescription("자동 로그인 테스트")
    public void loginMember_auto() throws Exception {
        /*
        MockCookie id = new MockCookie(CookieName.ID.getName(), userMember.getId());
        MockCookie name = new MockCookie(CookieName.NAME.getName(), userMember.getName());
        MockCookie phoneNum = new MockCookie(CookieName.PHONE_NUM.getName(), userMember.getPhoneNumber());
        MockCookie isWorker = new MockCookie(CookieName.IS_WORKER.getName(), Integer.toString(userMember.getIsWorker()));
        MockCookie sessionId = new MockCookie(CookieName.SESSION_ID.getName(), Integer.toString(1));
        MockCookie isAdmin = new MockCookie(CookieName.IS_ADMIN.getName(), Integer.toString(userMember.getIsAdmin()));
        MockCookie isAutoLogin = new MockCookie(CookieName.IS_AUTO_LOGIN.getName(), "true");

        ResultActions result = mockMvc.perform(
                RestDocumentationRequestBuilders.post("/api/member/autoLogin")
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
                .andExpect(cookie().exists(CookieName.ID.getName()))
                .andExpect(cookie().exists(CookieName.NAME.getName()))
                .andExpect(cookie().exists(CookieName.PHONE_NUM.getName()))
                .andExpect(cookie().exists(CookieName.IS_WORKER.getName()))
                .andExpect(cookie().exists(CookieName.SESSION_ID.getName()))
                .andExpect(cookie().exists(CookieName.IS_ADMIN.getName()))
                .andExpect(cookie().exists(CookieName.IS_AUTO_LOGIN.getName()));
*/

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
