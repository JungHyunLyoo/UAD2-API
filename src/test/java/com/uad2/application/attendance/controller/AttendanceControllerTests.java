package com.uad2.application.attendance.controller;
/*
 * @USER JungHyun
 * @DATE 2019-11-13
 * @DESCRIPTION
 */

import com.uad2.application.BaseControllerTest;
import com.uad2.application.attendance.dto.AttendanceDto;
import com.uad2.application.common.TestDescription;
import org.junit.Test;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockCookie;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import javax.transaction.Transactional;

import static com.uad2.application.api.document.utils.DocumentFormatGenerator.getDateFormat;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureRestDocs(outputDir = "target/generated-snippets/attendance")
public class AttendanceControllerTests extends BaseControllerTest {
    @Test
    @Transactional
    @TestDescription("특정일 참가 내역 조회 성공")
    public void getAllAttendanceList() throws Exception {
        MockHttpServletResponse response = super.execLogin("testUser","testUser",false).andReturn().getResponse();

        MockCookie[] cookieList = super.convertCookieToMockCookie(response.getCookies());

        String[] paramList = new String[]{"2019-11-09"};

        // request
        ResultActions result = mockMvc.perform(
                super.getRequest("/api/attendance/date/{date}",paramList, cookieList)
                        .accept(MediaTypes.HAL_JSON)
        );
        // result
        result.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("attendanceList").isNotEmpty())
                .andDo(document("getAllAttendanceList",
                        pathParameters(
                                parameterWithName("date").description("참가일 (yyyy-MM-dd)")
                        ),
                        links(
                                linkWithRel("profile").description("restDoc link")
                        ),
                        responseFields(
                                subsectionWithPath("attendanceList").description("참가 회원 데이터 리스트"),
                                subsectionWithPath("_links").description("링크"),
                                fieldWithPath("attendanceList[].member").type(JsonFieldType.OBJECT).description("회원 정보"),
                                fieldWithPath("attendanceList[].availableTime").type(JsonFieldType.STRING).description("참가 신청 시간"),
                                fieldWithPath("attendanceList[].availableDate").type(JsonFieldType.STRING).attributes(getDateFormat()).description("참가 신청 일자")
                        )
                ));
    }
    @Test
    @Transactional
    @TestDescription("특정일 참가 내역 조회 에러(로그인 x)")
    public void getAllAttendanceList_badRequest_noAuth() throws Exception {
        String[] paramList = new String[]{"2019-11-10"};
        // request
        ResultActions result = mockMvc.perform(
                super.getRequest("/api/attendance/date/{date}",paramList)
        );
        // result
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("message").value("Cookie is not exist"))
                .andDo(print());
    }
    @Test
    @Transactional
    @TestDescription("특정일 참가 내역 조회 에러(파라미터 오류)")
    public void getAllAttendanceList_badRequest_invalidParameter() throws Exception {
        MockHttpServletResponse response = super.execLogin("testUser","testUser",false).andReturn().getResponse();

        MockCookie[] cookieList = super.convertCookieToMockCookie(response.getCookies());

        String[] paramList = new String[]{"201911"};

        // request
        ResultActions result = mockMvc.perform(
                super.getRequest("/api/attendance/date/{date}",paramList, cookieList)
                        .accept(MediaTypes.HAL_JSON)
        );

        // result
        result.andExpect(status().isBadRequest())
                .andDo(print());


        paramList = new String[]{"20191110"};
        // request
        result = mockMvc.perform(
                super.getRequest("/api/attendance/date/{date}",paramList, cookieList)
                        .accept(MediaTypes.HAL_JSON)
        );
        // result
        result.andExpect(status().isBadRequest())
                .andDo(print());

        paramList = new String[]{"2019-13"};
        // request
        result = mockMvc.perform(
                super.getRequest("/api/attendance/date/{date}",paramList, cookieList)
                        .accept(MediaTypes.HAL_JSON)
        );
        // result
        result.andExpect(status().isBadRequest())
                .andDo(print());


        paramList = new String[]{"2019-13-00"};
        // request
        result = mockMvc.perform(
                super.getRequest("/api/attendance/date/{date}",paramList, cookieList)
                        .accept(MediaTypes.HAL_JSON)
        );
        // result
        result.andExpect(status().isBadRequest())
                .andDo(print());
    }
    @Test
    @Transactional
    @TestDescription("참가 데이터 생성 성공(매칭 업데이트 x)")
    public void createAttendance_notUpdateMatching() throws Exception {
        MockHttpServletResponse response = super.execLogin("testUser","testUser",false).andReturn().getResponse();

        MockCookie[] cookieList = super.convertCookieToMockCookie(response.getCookies());

        AttendanceDto.Request attendanceRequest = AttendanceDto.Request.builder()
                .availableDate("2019-11-10")
                .availableTime("1,2")
                .build();

        // request
        ResultActions result = mockMvc.perform(
                super.postRequest("/api/attendance",attendanceRequest,cookieList)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaTypes.HAL_JSON)
        );

        // result
        result.andExpect(status().isCreated())
                .andDo(print())
                .andDo(document("createAttendanceNotUpdateMatching",
                        requestFields(
                                fieldWithPath("availableDate").type(JsonFieldType.STRING).description("날짜(ex 2019-11-10)"),
                                fieldWithPath("availableTime").type(JsonFieldType.STRING).description("참가 시간대")
                        ),
                        responseFields(
                                subsectionWithPath("attendance").description("참가"),
                                subsectionWithPath("_links").description("링크"),
                                fieldWithPath("attendance.member").type(JsonFieldType.OBJECT).description("회원 정보"),
                                fieldWithPath("attendance.availableTime").type(JsonFieldType.STRING).description("참가 신청 시간"),
                                fieldWithPath("attendance.availableDate").type(JsonFieldType.STRING).attributes(getDateFormat()).description("참가 신청 일자")
                        )
                ));
    }
    @Test
    @Transactional
    @TestDescription("참가 데이터 생성 성공(매칭 업데이트 o)")
    public void createAttendance_updateMatching_addMember() throws Exception {
        MockHttpServletResponse response = super.execLogin("testUser","testUser",false).andReturn().getResponse();

        MockCookie[] cookieList = super.convertCookieToMockCookie(response.getCookies());

        AttendanceDto.Request attendanceRequest = AttendanceDto.Request.builder()
                .availableDate("2019-11-02")
                .availableTime("5,6")
                .build();
        // request
        ResultActions result = mockMvc.perform(
                super.postRequest("/api/attendance",attendanceRequest,cookieList)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaTypes.HAL_JSON)
        );
        // result
        result.andExpect(status().isCreated())
                .andDo(print())
                .andDo(document("createAttendanceUpdateMatching",
                        requestFields(
                                fieldWithPath("availableTime").type(JsonFieldType.STRING).description("참가 신청 시간"),
                                fieldWithPath("availableDate").type(JsonFieldType.STRING).description("참가 신청 일자 (yyyy-MM-dd)")
                        ),
                        links(
                                linkWithRel("profile").description("restDoc link")
                        ),
                        responseFields(
                                subsectionWithPath("attendance").description("참가 회원 데이터"),
                                subsectionWithPath("_links").description("링크"),
                                fieldWithPath("attendance.member").type(JsonFieldType.OBJECT).description("회원 정보"),
                                fieldWithPath("attendance.availableTime").type(JsonFieldType.STRING).description("참가 신청 시간"),
                                fieldWithPath("attendance.availableDate").type(JsonFieldType.STRING).attributes(getDateFormat()).description("참가 신청 일자")
                        )
                ));
    }
    @Test
    @Transactional
    @TestDescription("참가 데이터 생성 에러(시간대 미기입)")
    public void createAttendance_error_emptyAvailableTime() throws Exception {
        MockHttpServletResponse response = super.execLogin("testUser","testUser",false).andReturn().getResponse();

        MockCookie[] cookieList = super.convertCookieToMockCookie(response.getCookies());

        AttendanceDto.Request attendanceRequest = AttendanceDto.Request.builder()
                .availableDate("2019-11-02")
                .build();
        // request
        ResultActions result = mockMvc.perform(
                super.postRequest("/api/attendance",attendanceRequest,cookieList)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaTypes.HAL_JSON)
        );
        // result
        result.andExpect(status().isBadRequest())
                .andDo(print());
    }
    @Test
    @Transactional
    @TestDescription("참가 데이터 수정 성공")
    public void updateAttendance() throws Exception {
        MockHttpServletResponse response = super.execLogin("testUser","testUser",false).andReturn().getResponse();

        MockCookie[] cookieList = super.convertCookieToMockCookie(response.getCookies());

        AttendanceDto.Request attendanceRequest = AttendanceDto.Request.builder()
                .availableDate("2019-11-09")
                .availableTime("5,6")
                .build();
        // request
        ResultActions result = mockMvc.perform(
                super.postRequest("/api/attendance",attendanceRequest,cookieList)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaTypes.HAL_JSON)
        );
        // result
        result.andExpect(status().isOk())
                .andDo(print())
                .andDo(document("updateAttendance",
                        requestFields(
                                fieldWithPath("availableTime").type(JsonFieldType.STRING).description("참가 신청 시간"),
                                fieldWithPath("availableDate").type(JsonFieldType.STRING).description("참가 신청 일자 (yyyy-MM-dd)")
                        ),
                        links(
                                linkWithRel("profile").description("restDoc link")
                        ),
                        responseFields(
                                subsectionWithPath("attendance").description("참가 회원 데이터"),
                                subsectionWithPath("_links").description("링크"),
                                fieldWithPath("attendance.member").type(JsonFieldType.OBJECT).description("회원 정보"),
                                fieldWithPath("attendance.availableTime").type(JsonFieldType.STRING).description("참가 신청 시간"),
                                fieldWithPath("attendance.availableDate").type(JsonFieldType.STRING).attributes(getDateFormat()).description("참가 신청 일자")
                        )
                ));

    }
    @Test
    @Transactional
    @TestDescription("참가 데이터 삭제 성공")
    public void deleteAttendance() throws Exception {
        MockHttpServletResponse response = super.execLogin("testUser","testUser",false).andReturn().getResponse();

        MockCookie[] cookieList = super.convertCookieToMockCookie(response.getCookies());

        AttendanceDto.Request attendanceRequest = AttendanceDto.Request.builder()
                .availableDate("2019-11-09")
                .availableTime("")
                .build();
        // request
        ResultActions result = mockMvc.perform(
                super.postRequest("/api/attendance",attendanceRequest,cookieList)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaTypes.HAL_JSON)
        );
        // result
        result.andExpect(status().isOk())
                .andDo(print())
                .andDo(document("deleteAttendance",
                        requestFields(
                                fieldWithPath("availableTime").type(JsonFieldType.STRING).description("참가 신청 시간"),
                                fieldWithPath("availableDate").type(JsonFieldType.STRING).description("참가 신청 일자 (yyyy-MM-dd)")
                        )
                ));

    }
    @Test
    @Transactional
    @TestDescription("참가 데이터 삭제 에러(기존 참가내역 x)")
    public void deleteAttendance_error_emptyAttendance() throws Exception {
        MockHttpServletResponse response = super.execLogin("testUser","testUser",false).andReturn().getResponse();

        MockCookie[] cookieList = super.convertCookieToMockCookie(response.getCookies());

        AttendanceDto.Request attendanceRequest = AttendanceDto.Request.builder()
                .availableDate("2019-11-02")
                .availableTime("")
                .build();
        // request
        ResultActions result = mockMvc.perform(
                super.postRequest("/api/attendance",attendanceRequest,cookieList)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaTypes.HAL_JSON)
        );
        // result
        result.andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @Transactional
    @TestDescription("특정 회원의 참가 데이터 조회 by memberSeq & date")
    public void getAttendance_byMemberSeqAndDate() throws Exception {
        MockHttpServletResponse response = super.execLogin("testUser","testUser",false).andReturn().getResponse();

        MockCookie[] cookieList = super.convertCookieToMockCookie(response.getCookies());
        String[] paramList = new String[]{"400", "2019-12-12"};

        // request
        ResultActions result = mockMvc.perform(
                super.getRequest("/api/attendance/memberSeq/{memberSeq}/date/{date}",paramList,cookieList)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaTypes.HAL_JSON)
        );
        // result
        result.andExpect(status().isOk())
                .andDo(print())
                .andDo(document("getAttendance_byMemberSeqAndDate",
                        pathParameters(
                                parameterWithName("memberSeq").description("회원 인덱스"),
                                parameterWithName("date").description("참가일 (yyyy-MM-dd)")
                        ),
                        links(
                                linkWithRel("profile").description("restDoc link")
                        ),
                        responseFields(
                                subsectionWithPath("attendance").description("참가 회원 데이터"),
                                subsectionWithPath("_links").description("링크"),
                                fieldWithPath("attendance.member").type(JsonFieldType.OBJECT).description("회원 정보"),
                                fieldWithPath("attendance.availableTime").type(JsonFieldType.STRING).description("참가 신청 시간"),
                                fieldWithPath("attendance.availableDate").type(JsonFieldType.STRING).attributes(getDateFormat()).description("참가 신청 일자")
                        )
                ));
    }

}
