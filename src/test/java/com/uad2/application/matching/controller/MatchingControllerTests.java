package com.uad2.application.matching.controller;
/*
 * @USER JungHyun
 * @DATE 2019-11-17
 * @DESCRIPTION
 */

import com.uad2.application.BaseControllerTest;
import com.uad2.application.attendance.dto.AttendanceDto;
import com.uad2.application.common.TestDescription;
import com.uad2.application.common.enumData.CookieName;
import com.uad2.application.matching.dto.MatchingDto;
import org.junit.Test;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockCookie;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.ResultActions;

import javax.transaction.Transactional;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class MatchingControllerTests extends BaseControllerTest {
    @Test
    @Transactional
    @TestDescription("월별 매칭 내역 조회")
    public void getMatching_monthly() throws Exception {
        String[] paramList = new String[]{"2019-11"};
        MockCookie[] userMemberCookieList = super.getUserMemberCookieList(AUTOLOGIN_FALSE);
        // request
        ResultActions result = mockMvc.perform(
                super.getRequest("/api/matching/date/{date}",paramList, userMemberCookieList)
                        .accept(MediaTypes.HAL_JSON)
        );
        // result
        result.andExpect(status().isOk())
                .andExpect(jsonPath("attendanceList").isNotEmpty())
                .andDo(print());
    }
    @Test
    @Transactional
    @TestDescription("월별 매칭 내역 조회_에러(파라미터 미입력)")
    public void getMatching_error_emptyParam() throws Exception {
        String[] paramList = new String[]{""};
        MockCookie[] userMemberCookieList = super.getUserMemberCookieList(AUTOLOGIN_FALSE);
        // request
        ResultActions result = mockMvc.perform(
                super.getRequest("/api/matching/date/{date}",paramList, userMemberCookieList)
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
        MockCookie[] userMemberCookieList = super.getUserMemberCookieList(AUTOLOGIN_FALSE);
        // request
        ResultActions result = mockMvc.perform(
                super.getRequest("/api/matching/date/{date}",paramList, userMemberCookieList)
                        .accept(MediaTypes.HAL_JSON)
        );
        // result
        result.andExpect(status().isOk())
                .andExpect(jsonPath("attendanceList").isNotEmpty())
                .andDo(print());
    }
    @Test
    @Transactional
    @TestDescription("매칭 생성")
    public void createMatching() throws Exception{
        MockCookie[] adminMemberCookieList = super.getAdminMemberCookieList(AUTOLOGIN_FALSE);
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
                super.postRequest("/api/matching",matchingRequest,adminMemberCookieList)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaTypes.HAL_JSON)
        );
        // result
        result.andExpect(status().isCreated())
                .andDo(print());

    }
    @Test
    @Transactional
    @TestDescription("매칭 생성 에러(파라미터 미입력)")
    public void createMatching_error_emptyParam() throws Exception{
        MockCookie[] adminMemberCookieList = super.getAdminMemberCookieList(AUTOLOGIN_FALSE);
        MatchingDto.Request matchingRequest = MatchingDto.Request.builder()
                .matchingTime("1,2")
                .matchingPlace("test place")
                .content("test content")
                .attendMember("1,2")
                .price(1000)
                .build();
        // request
        ResultActions result = mockMvc.perform(
                super.postRequest("/api/matching",matchingRequest,adminMemberCookieList)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaTypes.HAL_JSON)
        );
        // result
        result.andExpect(status().isAccepted())
                .andDo(print());
    }
    @Test
    @Transactional
    @TestDescription("매칭 수정")
    public void updateMatching() throws Exception{
        MockCookie[] adminMemberCookieList = super.getAdminMemberCookieList(AUTOLOGIN_FALSE);
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
                super.postRequest("/api/matching",matchingRequest,adminMemberCookieList)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaTypes.HAL_JSON)
        );
        // result
        result.andExpect(status().isCreated())
                .andDo(print());
    }
    @Test
    @Transactional
    @TestDescription("매칭 삭제")
    public void removeMatching() throws Exception{

    }
    @Test
    @Transactional
    @TestDescription("매칭 삭제 에러(파라미터 미입력)")
    public void removeMatching_error_emptyParam() throws Exception{

    }
}
