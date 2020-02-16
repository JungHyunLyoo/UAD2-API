package com.uad2.application.matching.controller;
/*
 * @USER JungHyun
 * @DATE 2019-11-17
 * @DESCRIPTION
 */

import com.uad2.application.BaseControllerTest;
import com.uad2.application.common.TestDescription;
import com.uad2.application.matching.dto.MatchingDto;
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
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureRestDocs(outputDir = "target/generated-snippets/matching")
public class MatchingControllerTests extends BaseControllerTest {
    @Test
    @Transactional
    @TestDescription("월별 매칭 내역 조회")
    public void getMatching_monthly() throws Exception {
        String[] paramList = new String[]{"2019-12"};
        MockHttpServletResponse response = super.execLogin("testUser","testUser",false).andReturn().getResponse();

        MockCookie[] cookieList = super.convertCookieToMockCookie(response.getCookies());
        // request
        ResultActions result = mockMvc.perform(
                super.getRequest("/api/matching/date/{date}",paramList, cookieList)
                        .accept(MediaTypes.HAL_JSON)
        );
        // result
        result.andExpect(status().isOk())
                .andExpect(jsonPath("attendanceList").isNotEmpty())
                .andDo(print())
                .andDo(document("getMatching_monthly",
                        pathParameters(
                                parameterWithName("date").description("조회 월 (yyyy-MM)")
                        ),
                        links(
                                linkWithRel("profile").description("restDoc link")
                        ),
                        responseFields(
                                subsectionWithPath("attendanceList").description("매칭 데이터 리스트"),
                                subsectionWithPath("_links").description("링크"),
                                fieldWithPath("attendanceList[].matchingDate").type(JsonFieldType.STRING).attributes(getDateFormat()).description("매칭일"),
                                fieldWithPath("attendanceList[].matchingTime").type(JsonFieldType.STRING).description("매칭 시간"),
                                fieldWithPath("attendanceList[].matchingPlace").type(JsonFieldType.STRING).description("매칭 장소"),
                                fieldWithPath("attendanceList[].content").type(JsonFieldType.STRING).description("매칭 설명"),
                                fieldWithPath("attendanceList[].attendMember").type(JsonFieldType.STRING).description("참가 회원"),
                                fieldWithPath("attendanceList[].maxCnt").type(JsonFieldType.NUMBER).description("최대 참석 가능 인원").optional(),
                                fieldWithPath("attendanceList[].price").type(JsonFieldType.NUMBER).description("매칭 금액")
                        )
                ));
    }
    @Test
    @Transactional
    @TestDescription("월별 매칭 내역 조회_에러(파라미터 미입력)")
    public void getMatching_error_emptyParam() throws Exception {
        MockHttpServletResponse response = super.execLogin("testUser","testUser",false).andReturn().getResponse();

        MockCookie[] cookieList = super.convertCookieToMockCookie(response.getCookies());
        String[] paramList = new String[]{""};
        // request
        ResultActions result = mockMvc.perform(
                super.getRequest("/api/matching/date/{date}",paramList, cookieList)
                        .accept(MediaTypes.HAL_JSON)
        );
        // result
        result.andExpect(status().isNotFound())
                .andDo(print());
    }
    @Test
    @Transactional
    @TestDescription("일별 매칭 내역 조회")
    public void getMatching_daily() throws Exception {
        String[] paramList = new String[]{"2019-11-02"};
        MockHttpServletResponse response = super.execLogin("testUser","testUser",false).andReturn().getResponse();

        MockCookie[] cookieList = super.convertCookieToMockCookie(response.getCookies());
        // request
        ResultActions result = mockMvc.perform(
                super.getRequest("/api/matching/date/{date}",paramList, cookieList)
                        .accept(MediaTypes.HAL_JSON)
        );
        // result
        result.andExpect(status().isOk())
                .andExpect(jsonPath("attendanceList").isNotEmpty())
                .andDo(print())
                .andDo(document("getMatching_daily",
                        pathParameters(
                                parameterWithName("date").description("조회 일 (yyyy-MM-dd)")
                        ),
                        links(
                                linkWithRel("profile").description("restDoc link")
                        ),
                        responseFields(
                                subsectionWithPath("attendanceList").description("매칭 데이터 리스트"),
                                subsectionWithPath("_links").description("링크"),
                                fieldWithPath("attendanceList[].matchingDate").type(JsonFieldType.STRING).attributes(getDateFormat()).description("매칭일"),
                                fieldWithPath("attendanceList[].matchingTime").type(JsonFieldType.STRING).description("매칭 시간"),
                                fieldWithPath("attendanceList[].matchingPlace").type(JsonFieldType.STRING).description("매칭 장소"),
                                fieldWithPath("attendanceList[].content").type(JsonFieldType.STRING).description("매칭 설명"),
                                fieldWithPath("attendanceList[].attendMember").type(JsonFieldType.STRING).description("참가 회원"),
                                fieldWithPath("attendanceList[].maxCnt").type(JsonFieldType.NUMBER).description("최대 참석 가능 인원").optional(),
                                fieldWithPath("attendanceList[].price").type(JsonFieldType.NUMBER).description("매칭 금액")
                        )
                ));
    }
    @Test
    @Transactional
    @TestDescription("매칭 생성")
    public void createMatching() throws Exception{
        MockHttpServletResponse response = super.execLogin("testAdmin","testAdmin",false).andReturn().getResponse();

        MockCookie[] cookieList = super.convertCookieToMockCookie(response.getCookies());
        MatchingDto.Request matchingRequest = MatchingDto.Request.builder()
                .matchingDate("2019-11-09")
                .matchingTime("1,2")
                .matchingPlace("test place")
                .content("test content")
                .attendMember("1,2")
                .price(1000)
                .build();
        // request
        ResultActions result = mockMvc.perform(
                super.postRequest("/api/matching",matchingRequest,cookieList)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaTypes.HAL_JSON)
        );
        // result
        result.andExpect(status().isCreated())
                .andDo(print())
                .andDo(document("createMatching",
                        requestFields(
                                fieldWithPath("matchingDate").type(JsonFieldType.STRING).attributes(getDateFormat()).description("매칭일 (yyyy-MM-dd)"),
                                fieldWithPath("matchingTime").type(JsonFieldType.STRING).description("매칭 시간"),
                                fieldWithPath("matchingPlace").type(JsonFieldType.STRING).description("매칭 장소"),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("매칭 설명"),
                                fieldWithPath("attendMember").type(JsonFieldType.STRING).description("참가 회원"),
                                fieldWithPath("price").type(JsonFieldType.NUMBER).description("매칭 금액")
                        ),
                        links(
                                linkWithRel("profile").description("restDoc link")
                        ),
                        responseFields(
                                subsectionWithPath("attendance").description("매칭 데이터"),
                                subsectionWithPath("_links").description("링크"),
                                fieldWithPath("attendance.matchingDate").type(JsonFieldType.STRING).attributes(getDateFormat()).description("매칭일"),
                                fieldWithPath("attendance.matchingTime").type(JsonFieldType.STRING).description("매칭 시간"),
                                fieldWithPath("attendance.matchingPlace").type(JsonFieldType.STRING).description("매칭 장소"),
                                fieldWithPath("attendance.content").type(JsonFieldType.STRING).description("매칭 설명"),
                                fieldWithPath("attendance.attendMember").type(JsonFieldType.STRING).description("참가 회원"),
                                fieldWithPath("attendance.maxCnt").type(JsonFieldType.NUMBER).description("최대 참석 가능 인원").optional(),
                                fieldWithPath("attendance.price").type(JsonFieldType.NUMBER).description("매칭 금액")
                        )
                ));

    }
    @Test
    @Transactional
    @TestDescription("매칭 생성 에러(파라미터 미입력)")
    public void createMatching_error_emptyParam() throws Exception{
        MockHttpServletResponse response = super.execLogin("testAdmin","testAdmin",false).andReturn().getResponse();

        MockCookie[] cookieList = super.convertCookieToMockCookie(response.getCookies());
        MatchingDto.Request matchingRequest = MatchingDto.Request.builder()
                .matchingTime("1,2")
                .matchingPlace("test place")
                .content("test content")
                .attendMember("1,2")
                .price(1000)
                .build();
        // request
        ResultActions result = mockMvc.perform(
                super.postRequest("/api/matching",matchingRequest,cookieList)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaTypes.HAL_JSON)
        );
        // result
        result.andExpect(status().isBadRequest())
                .andDo(print());
    }
    @Test
    @Transactional
    @TestDescription("매칭 수정")
    public void updateMatching() throws Exception{
        MockHttpServletResponse response = super.execLogin("testAdmin","testAdmin",false).andReturn().getResponse();

        MockCookie[] cookieList = super.convertCookieToMockCookie(response.getCookies());
        MatchingDto.Request matchingRequest = MatchingDto.Request.builder()
                .matchingDate("2019-11-09")
                .matchingTime("1,2")
                .matchingPlace("test place")
                .content("test content")
                .attendMember("1,2")
                .price(1000)
                .build();
        // request
        ResultActions result = mockMvc.perform(
                super.postRequest("/api/matching",matchingRequest,cookieList)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaTypes.HAL_JSON)
        );
        // result
        result.andExpect(status().isCreated())
                .andDo(print())
                .andDo(document("updateMatching",
                        requestFields(
                                fieldWithPath("matchingDate").type(JsonFieldType.STRING).attributes(getDateFormat()).description("매칭일 (yyyy-MM-dd)"),
                                fieldWithPath("matchingTime").type(JsonFieldType.STRING).description("매칭 시간"),
                                fieldWithPath("matchingPlace").type(JsonFieldType.STRING).description("매칭 장소"),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("매칭 설명"),
                                fieldWithPath("attendMember").type(JsonFieldType.STRING).description("참가 회원"),
                                fieldWithPath("price").type(JsonFieldType.NUMBER).description("매칭 금액")
                        ),
                        links(
                                linkWithRel("profile").description("restDoc link")
                        ),
                        responseFields(
                                subsectionWithPath("attendance").description("매칭 데이터"),
                                subsectionWithPath("_links").description("링크"),
                                fieldWithPath("attendance.matchingDate").type(JsonFieldType.STRING).attributes(getDateFormat()).description("매칭일"),
                                fieldWithPath("attendance.matchingTime").type(JsonFieldType.STRING).description("매칭 시간"),
                                fieldWithPath("attendance.matchingPlace").type(JsonFieldType.STRING).description("매칭 장소"),
                                fieldWithPath("attendance.content").type(JsonFieldType.STRING).description("매칭 설명"),
                                fieldWithPath("attendance.attendMember").type(JsonFieldType.STRING).description("참가 회원"),
                                fieldWithPath("attendance.maxCnt").type(JsonFieldType.NUMBER).description("최대 참석 가능 인원").optional(),
                                fieldWithPath("attendance.price").type(JsonFieldType.NUMBER).description("매칭 금액")
                        )
                ));
    }
}
