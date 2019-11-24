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
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockCookie;
import org.springframework.test.web.servlet.ResultActions;

import javax.transaction.Transactional;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AttendanceControllerTests extends BaseControllerTest {
    @Test
    @Transactional
    @TestDescription("특정일 참가 내역 조회")
    public void getAllAttendanceList() throws Exception {
        String[] paramList = new String[]{"2019-11-10"};
        MockCookie[] userMemberCookieList = super.getUserMemberCookieList(AUTOLOGIN_FALSE);
        // request
        ResultActions result = mockMvc.perform(
                super.getRequest("/api/attendance/date/{date}",paramList, userMemberCookieList)
                        .accept(MediaTypes.HAL_JSON)
        );
        // result
        result.andExpect(status().isOk())
                .andDo(print());
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
        result.andExpect(status().isAccepted())
                .andDo(print());
    }
    @Test
    @Transactional
    @TestDescription("특정일 참가 내역 조회 에러(파라미터 오류1)")
    public void getAllAttendanceList_badRequest_invalidParameter1() throws Exception {
        String[] paramList = new String[]{"201911"};
        MockCookie[] userMemberCookieList = super.getUserMemberCookieList(AUTOLOGIN_FALSE);
        // request
        ResultActions result = mockMvc.perform(
                super.getRequest("/api/attendance/date/{date}",paramList, userMemberCookieList)
                        .accept(MediaTypes.HAL_JSON)
        );
        // result
        result.andExpect(status().isAccepted())
                .andDo(print());
    }
    @Test
    @Transactional
    @TestDescription("특정일 참가 내역 조회 에러(파라미터 오류2)")
    public void getAllAttendanceList_badRequest_invalidParameter2() throws Exception {
        String[] paramList = new String[]{"20191110"};
        MockCookie[] userMemberCookieList = super.getUserMemberCookieList(AUTOLOGIN_FALSE);
        // request
        ResultActions result = mockMvc.perform(
                super.getRequest("/api/attendance/date/{date}",paramList, userMemberCookieList)
                        .accept(MediaTypes.HAL_JSON)
        );
        // result
        result.andExpect(status().isAccepted())
                .andDo(print());
    }
    @Test
    @Transactional
    @TestDescription("특정일 참가 내역 조회 에러(파라미터 오류3)")
    public void getAllAttendanceList_badRequest_invalidParameter3() throws Exception {
        String[] paramList = new String[]{"2019-13"};
        MockCookie[] userMemberCookieList = super.getUserMemberCookieList(AUTOLOGIN_FALSE);
        // request
        ResultActions result = mockMvc.perform(
                super.getRequest("/api/attendance/date/{date}",paramList, userMemberCookieList)
                        .accept(MediaTypes.HAL_JSON)
        );
        // result
        result.andExpect(status().isAccepted())
                .andDo(print());
    }
    @Test
    @Transactional
    @TestDescription("특정일 참가 내역 조회 에러(파라미터 오류4)")
    public void getAllAttendanceList_badRequest_invalidParameter4() throws Exception {
        String[] paramList = new String[]{"2019-13-00"};
        MockCookie[] userMemberCookieList = super.getUserMemberCookieList(AUTOLOGIN_FALSE);
        // request
        ResultActions result = mockMvc.perform(
                super.getRequest("/api/attendance/date/{date}",paramList, userMemberCookieList)
                        .accept(MediaTypes.HAL_JSON)
        );
        // result
        result.andExpect(status().isAccepted())
                .andDo(print());
    }
    @Test
    @Transactional
    @TestDescription("참가 데이터 생성(매칭 업데이트 x)")
    public void createAttendance_notUpdateMatching() throws Exception {
        MockCookie[] userMemberCookieList = super.getUserMemberCookieList(AUTOLOGIN_FALSE);
        AttendanceDto.Request attendanceRequest = AttendanceDto.Request.builder()
                .availableDate("2019-11-10")
                .availableTime("1,2")
                .memberSeq(userMember.getSeq())
                .build();
        // request
        ResultActions result = mockMvc.perform(
                super.postRequest("/api/attendance",attendanceRequest,userMemberCookieList)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaTypes.HAL_JSON)
        );
        // result
        result.andExpect(status().isCreated())
                .andDo(print());
    }
    @Test
    @Transactional
    @TestDescription("참가 데이터 생성(매칭 업데이트 o, 매칭에 회원 추가)")
    public void createAttendance_updateMatching_addMember() throws Exception {
        MockCookie[] userMemberCookieList = super.getUserMemberCookieList(AUTOLOGIN_FALSE);
        AttendanceDto.Request attendanceRequest = AttendanceDto.Request.builder()
                .availableDate("2019-11-02")
                .availableTime("5,6")
                .memberSeq(userMember.getSeq())
                .build();
        // request
        ResultActions result = mockMvc.perform(
                super.postRequest("/api/attendance",attendanceRequest,userMemberCookieList)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaTypes.HAL_JSON)
        );
        // result
        result.andExpect(status().isCreated())
                .andDo(print());
    }
    @Test
    @Transactional
    @TestDescription("참가 데이터 생성 에러(요청 회원 불일치)")
    public void createAttendance_error_notValidMember() throws Exception {
        MockCookie[] userMemberCookieList = super.getUserMemberCookieList(AUTOLOGIN_FALSE);
        AttendanceDto.Request attendanceRequest = AttendanceDto.Request.builder()
                .availableDate("2019-11-02")
                .availableTime("5,6")
                .memberSeq(1)
                .build();
        // request
        ResultActions result = mockMvc.perform(
                super.postRequest("/api/attendance",attendanceRequest,userMemberCookieList)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaTypes.HAL_JSON)
        );
        // result
        result.andExpect(status().isAccepted())
                .andDo(print());
    }
    @Test
    @Transactional
    @TestDescription("참가 데이터 생성 에러(요청 회원 미요청)")
    public void createAttendance_error_emptyMember() throws Exception {
        MockCookie[] userMemberCookieList = super.getUserMemberCookieList(AUTOLOGIN_FALSE);
        AttendanceDto.Request attendanceRequest = AttendanceDto.Request.builder()
                .availableDate("2019-11-02")
                .availableTime("5,6")
                .build();
        // request
        ResultActions result = mockMvc.perform(
                super.postRequest("/api/attendance",attendanceRequest,userMemberCookieList)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaTypes.HAL_JSON)
        );
        // result
        result.andExpect(status().isAccepted())
                .andDo(print());
    }
    @Test
    @Transactional
    @TestDescription("참가 데이터 생성 에러(시간대 미기입)")
    public void createAttendance_error_emptyAvailableTime() throws Exception {
        MockCookie[] userMemberCookieList = super.getUserMemberCookieList(AUTOLOGIN_FALSE);
        AttendanceDto.Request attendanceRequest = AttendanceDto.Request.builder()
                .availableDate("2019-11-02")
                .memberSeq(userMember.getSeq())
                .build();
        // request
        ResultActions result = mockMvc.perform(
                super.postRequest("/api/attendance",attendanceRequest,userMemberCookieList)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaTypes.HAL_JSON)
        );
        // result
        result.andExpect(status().isAccepted())
                .andDo(print());
    }
    @Test
    @Transactional
    @TestDescription("참가 데이터 생성 에러(참가 삭제요청, 그런데 기존 참가내역 존재 x)")
    public void createAttendance_error_emptyAttendance() throws Exception {
        MockCookie[] userMemberCookieList = super.getUserMemberCookieList(AUTOLOGIN_FALSE);
        AttendanceDto.Request attendanceRequest = AttendanceDto.Request.builder()
                .availableDate("2019-11-02")
                .availableTime("")
                .memberSeq(userMember.getSeq())
                .build();
        // request
        ResultActions result = mockMvc.perform(
                super.postRequest("/api/attendance",attendanceRequest,userMemberCookieList)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaTypes.HAL_JSON)
        );
        // result
        result.andExpect(status().isAccepted())
                .andDo(print());
    }
    @Test
    @Transactional
    @TestDescription("참가 데이터 수정")
    public void updateAttendance() throws Exception {
        MockCookie[] userMemberCookieList = super.getUserMemberCookieList(AUTOLOGIN_FALSE);
        AttendanceDto.Request attendanceRequest = AttendanceDto.Request.builder()
                .availableDate("2019-11-09")
                .availableTime("5,6")
                .memberSeq(userMember.getSeq())
                .build();
        // request
        ResultActions result = mockMvc.perform(
                super.postRequest("/api/attendance",attendanceRequest,userMemberCookieList)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaTypes.HAL_JSON)
        );
        // result
        result.andExpect(status().isOk())
                .andDo(print());
    }
    @Test
    @Transactional
    @TestDescription("참가 데이터 삭제")
    public void deleteAttendance() throws Exception {
        MockCookie[] userMemberCookieList = super.getUserMemberCookieList(AUTOLOGIN_FALSE);
        AttendanceDto.Request attendanceRequest = AttendanceDto.Request.builder()
                .availableDate("2019-11-09")
                .availableTime("")
                .memberSeq(userMember.getSeq())
                .build();
        // request
        ResultActions result = mockMvc.perform(
                super.postRequest("/api/attendance",attendanceRequest,userMemberCookieList)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaTypes.HAL_JSON)
        );
        // result
        result.andExpect(status().isNoContent())
                .andDo(print());
    }
}
