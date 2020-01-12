package com.uad2.application.member.controller;

/*
 * @USER JungHyun
 * @DATE 2019-09-09
 */

import com.uad2.application.BaseControllerTest;
import com.uad2.application.common.TestDescription;
import com.uad2.application.common.enumData.CookieName;
import com.uad2.application.member.dto.MemberDto;
import com.uad2.application.utils.CookieUtil;
import org.junit.Test;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockCookie;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import javax.transaction.Transactional;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

import static com.uad2.application.api.document.utils.DocumentFormatGenerator.getDateFormat;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureRestDocs(outputDir = "target/generated-snippets/member")
public class MemberControllerTests extends BaseControllerTest {

    @Test
    @Transactional
    @TestDescription("전체 회원 조회")
    public void getAllMember() throws Exception {
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
                .andDo(document("getAllMember",//조각 모음 폴더의 이름
                        links(
                                linkWithRel("profile").description("restDoc link")
                        ),
                        responseFields(
                                subsectionWithPath("memberList").description("전체 회원 데이터 리스트"),
                                subsectionWithPath("_links").description("링크"),
                                fieldWithPath("memberList[].seq").description("인덱스").type(JsonFieldType.NUMBER),
                                fieldWithPath("memberList[].id").description("아이디").type(JsonFieldType.STRING),
                                fieldWithPath("memberList[].name").description("이름").type(JsonFieldType.STRING),
                                fieldWithPath("memberList[].profileImg").description("프로필 이미지").type(JsonFieldType.STRING).optional(),
                                fieldWithPath("memberList[].attdCnt").description("누적 참석 횟수").type(JsonFieldType.NUMBER),
                                fieldWithPath("memberList[].birthDay").description("생년월일").type(JsonFieldType.STRING),
                                fieldWithPath("memberList[].studentId").description("학번").type(JsonFieldType.NUMBER),
                                fieldWithPath("memberList[].isWorker").description("직장인 여부").type(JsonFieldType.NUMBER),
                                fieldWithPath("memberList[].phoneNumber").description("핸드폰 번호").type(JsonFieldType.STRING).optional(),
                                fieldWithPath("memberList[].sessionId").description("세션 ID").type(JsonFieldType.STRING).optional(),
                                fieldWithPath("memberList[].sessionLimit").description("세션 만료일").type(JsonFieldType.STRING).optional(),
                                fieldWithPath("memberList[].isAdmin").description("관리자 권한 여부").type(JsonFieldType.NUMBER),
                                fieldWithPath("memberList[].isBenefit").description("특혜 (회비 면제)").type(JsonFieldType.NUMBER)
                        )
                ));
    }
    @Test
    @Transactional
    @TestDescription("전체 회원 조회 에러(일반 유저 로그인)")
    public void getAllMember_badRequest_noAuth() throws Exception {
        MockCookie[] userMemberCookieList = super.getUserMemberCookieList(AUTOLOGIN_FALSE);
        // request
        ResultActions result = mockMvc.perform(
                super.getRequest("/api/member", userMemberCookieList)
                        .accept(MediaTypes.HAL_JSON)
        );
        // result
        result.andExpect(status().isForbidden())
                .andExpect(jsonPath("message").exists())
                .andExpect(jsonPath("message").value("Member auth is not valid"))
                .andDo(print());
    }
    @Test
    @Transactional
    @TestDescription("전체 회원 조회 에러(로그인 x)")
    public void getAllMember_badRequest_noLogin() throws Exception {
        // request
        ResultActions result = mockMvc.perform(
                super.getRequest("/api/member")
                        .accept(MediaTypes.HAL_JSON)
        );
        // result
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("message").exists())
                .andExpect(jsonPath("message").value("Cookies are not exist"))
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
                                subsectionWithPath("_links").description("링크"),
                                fieldWithPath("member.seq").description("인덱스").type(JsonFieldType.NUMBER),
                                fieldWithPath("member.id").description("아이디").type(JsonFieldType.STRING),
                                fieldWithPath("member.name").description("이름").type(JsonFieldType.STRING),
                                fieldWithPath("member.profileImg").description("프로필 이미지").type(JsonFieldType.STRING).optional(),
                                fieldWithPath("member.attdCnt").description("누적 참석 횟수").type(JsonFieldType.NUMBER),
                                fieldWithPath("member.birthDay").description("생년월일").type(JsonFieldType.STRING),
                                fieldWithPath("member.studentId").description("학번").type(JsonFieldType.NUMBER),
                                fieldWithPath("member.isWorker").description("직장인 여부").type(JsonFieldType.NUMBER),
                                fieldWithPath("member.phoneNumber").description("핸드폰 번호").type(JsonFieldType.STRING).optional(),
                                fieldWithPath("member.sessionId").description("세션 ID").type(JsonFieldType.STRING).optional(),
                                fieldWithPath("member.sessionLimit").description("세션 만료일").type(JsonFieldType.STRING).optional(),
                                fieldWithPath("member.isAdmin").description("관리자 권한 여부").type(JsonFieldType.NUMBER),
                                fieldWithPath("member.isBenefit").description("특혜 (회비 면제)").type(JsonFieldType.NUMBER)
                        )
                ));
    }
    @Test
    @Transactional
    @TestDescription("멤버 개별 조회(해당 아이디로 데이터 x) by id")
    public void getMemberById_emptyResult() throws Exception {
        String[] paramList = new String[]{"testUser1234"};
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
                .andDo(document("getMemberByIdEmptyResult",
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
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("message").exists())
                .andExpect(jsonPath("message").value("Cookies are not exist"))
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
                .andDo(print());
    }
    @Test
    @Transactional
    @TestDescription("회원 생성")
    public void createMember() throws Exception {
        // request
        ResultActions result = mockMvc.perform(
                RestDocumentationRequestBuilders
                        .fileUpload("/api/member")
                        .file(new MockMultipartFile("profileImg", new FileInputStream("./uad.png")))
                        .param("id","test")
                        .param("pwd","test")
                        .param("name","test")
                        .param("birthDay","2020-01-01")
                        .param("studentId","11")
                        .param("isWorker","0")
                        .param("phoneNumber","1121")
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                        .accept(MediaTypes.HAL_JSON)
        );
        // result
        result.andExpect(status().isCreated())
                .andDo(print())
                .andExpect(jsonPath("member").exists())
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_UTF8_VALUE))
                .andDo(document("createMember",
                        requestParameters(
                                parameterWithName("id").description("아이디"),
                                parameterWithName("pwd").description("비밀번호"),
                                parameterWithName("name").description("이름"),
                                parameterWithName("birthDay").description("생일(yyyy-MM-dd)"),
                                parameterWithName("id").description("아이디"),
                                parameterWithName("id").description("아이디"),
                                parameterWithName("studentId").description("학번"),
                                parameterWithName("isWorker").description("직장인 여부"),
                                parameterWithName("phoneNumber").description("핸드폰 번호"),
                                parameterWithName("id").description("아이디")
                        ),
                        requestParts(
                                partWithName("profileImg").description("프로필 이미지")
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
        // request
        ResultActions result = mockMvc.perform(
                RestDocumentationRequestBuilders
                        .fileUpload("/api/member")
                        .file(new MockMultipartFile("profileImg", new FileInputStream("./uad.png")))
                        .param("pwd","test")
                        .param("name","test")
                        .param("birthDay","2020-01-01")
                        .param("studentId","11")
                        .param("isWorker","0")
                        .param("phoneNumber","1121")
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                        .accept(MediaTypes.HAL_JSON)
        );
        // result
        result.andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(jsonPath("message").exists())
                .andExpect(jsonPath("message").value("id is empty"));
    }
    @Test
    @Transactional
    @TestDescription("수동 로그인")
    public void loginMember_isAutoLogin_false() throws Exception {
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
                .andExpect(cookie().exists(CookieName.IS_ADMIN.getName()))
                .andDo(document("loginMemberAutoLoginFalse",
                        requestFields(
                                fieldWithPath("id").type(JsonFieldType.STRING).description("아이디"),
                                fieldWithPath("pwd").type(JsonFieldType.STRING).description("비밀번호"),
                                fieldWithPath("isAutoLogin").type(JsonFieldType.BOOLEAN).description("자동로그인 여부")
                        )
                ));
    }
    @Test
    @Transactional
    @TestDescription("수동 로그인 아이디 오류")
    public void loginMember_general_isAutoLogin_false_badRequest_invalidId() throws Exception {
        MemberDto.LoginRequest loginRequest = MemberDto.LoginRequest.builder()
                .id("testUser1235")
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
        result.andExpect(status().isBadRequest())
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
    @TestDescription("수동 로그인 비밀번호 오류")
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
        result.andExpect(status().isBadRequest())
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
    public void loginMember_isAutoLogin_true() throws Exception {
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
                .andExpect(cookie().maxAge(CookieName.IS_ADMIN.getName(), CookieUtil.A_YEAR_EXPIRATION))
                .andDo(document("loginMemberAutoLoginTrue",
                        requestFields(
                                fieldWithPath("id").type(JsonFieldType.STRING).attributes(getDateFormat()).description("아이디"),
                                fieldWithPath("pwd").type(JsonFieldType.STRING).description("비밀번호"),
                                fieldWithPath("isAutoLogin").type(JsonFieldType.BOOLEAN).description("자동로그인 여부")
                        )
                ));
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
        result.andExpect(status().isBadRequest())
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
        result.andExpect(status().isBadRequest())
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
    @TestDescription("비밀번호 체크 테스트")
    public void checkPwd_when_usingFor_updateProfile() throws Exception {
        MockCookie[] adminMemberCookieList = super.getAdminMemberCookieList(AUTOLOGIN_FALSE);

        Map<String, String> request = new HashMap<>();
        request.put("id", "testUser");
        request.put("pwd", "12345678");

        // request
        ResultActions result = mockMvc.perform(
                super.postRequest("/api/member/checkPwd", request, adminMemberCookieList)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );
        // result
        result.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("isSamePwd").exists())
                .andDo(document("checkPwd",
                        requestFields(
                                fieldWithPath("id").type(JsonFieldType.STRING).description("아이디"),
                                fieldWithPath("pwd").type(JsonFieldType.STRING).description("비밀번호")
                        ),
                        responseFields(
                                fieldWithPath("isSamePwd").description("비밀번호 일치 여부").type(JsonFieldType.BOOLEAN)
                        )
                ));
    }
}
