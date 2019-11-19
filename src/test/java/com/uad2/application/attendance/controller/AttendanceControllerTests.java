package com.uad2.application.attendance.controller;
/*
 * @USER JungHyun
 * @DATE 2019-11-13
 * @DESCRIPTION
 */

import com.uad2.application.BaseControllerTest;
import com.uad2.application.attendance.dto.AttendanceDto;
import com.uad2.application.common.TestDescription;
import com.uad2.application.common.enumData.CookieName;
import org.junit.Test;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockCookie;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.ResultActions;

import javax.transaction.Transactional;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AttendanceControllerTests extends BaseControllerTest {



    @Test
    @Transactional
    @TestDescription("특정일 참가 내역 조회")
    public void getAllAttendanceList() throws Exception {

        MockCookie id = new MockCookie(CookieName.ID.getName(), userMember.getId());
        MockCookie name = new MockCookie(CookieName.NAME.getName(), userMember.getName());
        MockCookie phoneNum = new MockCookie(CookieName.PHONE_NUM.getName(), userMember.getPhoneNumber());
        MockCookie isWorker = new MockCookie(CookieName.IS_WORKER.getName(), Integer.toString(userMember.getIsWorker()));
        MockCookie sessionId = new MockCookie(CookieName.SESSION_ID.getName(), userMember.getSessionId());
        MockCookie isAdmin = new MockCookie(CookieName.IS_ADMIN.getName(), Integer.toString(userMember.getIsAdmin()));
        MockCookie isAutoLogin = new MockCookie(CookieName.IS_AUTO_LOGIN.getName(), "false");

        // request
        ResultActions result = mockMvc.perform(
                RestDocumentationRequestBuilders.get("/api/attendance/date/{date}","2019-11-10")
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

    @Test
    @Transactional
    @TestDescription("특정일 참가 내역 조회 에러(로그인 x)")
    public void getAllAttendanceList_badRequest_noAuth() throws Exception {
        // request
        ResultActions result = mockMvc.perform(
                RestDocumentationRequestBuilders.get("/api/attendance/date/{date}","2019-11-10")
        );
        // result
        result.andExpect(status().isAccepted())
                .andDo(print());
    }


    @Test
    @Transactional
    @TestDescription("특정일 참가 내역 조회 에러(파라미터 오류1)")
    public void getAllAttendanceList_badRequest_invalidParameter1() throws Exception {

        MockCookie id = new MockCookie(CookieName.ID.getName(), userMember.getId());
        MockCookie name = new MockCookie(CookieName.NAME.getName(), userMember.getName());
        MockCookie phoneNum = new MockCookie(CookieName.PHONE_NUM.getName(), userMember.getPhoneNumber());
        MockCookie isWorker = new MockCookie(CookieName.IS_WORKER.getName(), Integer.toString(userMember.getIsWorker()));
        MockCookie sessionId = new MockCookie(CookieName.SESSION_ID.getName(), userMember.getSessionId());
        MockCookie isAdmin = new MockCookie(CookieName.IS_ADMIN.getName(), Integer.toString(userMember.getIsAdmin()));
        MockCookie isAutoLogin = new MockCookie(CookieName.IS_AUTO_LOGIN.getName(), "false");

        // request
        ResultActions result = mockMvc.perform(
                RestDocumentationRequestBuilders.get("/api/attendance/date/{date}","201911")
                        .cookie(id)
                        .cookie(name)
                        .cookie(phoneNum)
                        .cookie(isWorker)
                        .cookie(sessionId)
                        .cookie(isAdmin)
                        .cookie(isAutoLogin)
        );
        // result
        result.andExpect(status().isAccepted())
                .andDo(print());
    }


    @Test
    @Transactional
    @TestDescription("특정일 참가 내역 조회 에러(파라미터 오류2)")
    public void getAllAttendanceList_badRequest_invalidParameter2() throws Exception {

        MockCookie id = new MockCookie(CookieName.ID.getName(), userMember.getId());
        MockCookie name = new MockCookie(CookieName.NAME.getName(), userMember.getName());
        MockCookie phoneNum = new MockCookie(CookieName.PHONE_NUM.getName(), userMember.getPhoneNumber());
        MockCookie isWorker = new MockCookie(CookieName.IS_WORKER.getName(), Integer.toString(userMember.getIsWorker()));
        MockCookie sessionId = new MockCookie(CookieName.SESSION_ID.getName(), userMember.getSessionId());
        MockCookie isAdmin = new MockCookie(CookieName.IS_ADMIN.getName(), Integer.toString(userMember.getIsAdmin()));
        MockCookie isAutoLogin = new MockCookie(CookieName.IS_AUTO_LOGIN.getName(), "false");

        // request
        ResultActions result = mockMvc.perform(
                RestDocumentationRequestBuilders.get("/api/attendance/date/{date}","20191110")
                        .cookie(id)
                        .cookie(name)
                        .cookie(phoneNum)
                        .cookie(isWorker)
                        .cookie(sessionId)
                        .cookie(isAdmin)
                        .cookie(isAutoLogin)
        );
        // result
        result.andExpect(status().isAccepted())
                .andDo(print());
    }

    @Test
    @Transactional
    @TestDescription("특정일 참가 내역 조회 에러(파라미터 오류3)")
    public void getAllAttendanceList_badRequest_invalidParameter3() throws Exception {

        MockCookie id = new MockCookie(CookieName.ID.getName(), userMember.getId());
        MockCookie name = new MockCookie(CookieName.NAME.getName(), userMember.getName());
        MockCookie phoneNum = new MockCookie(CookieName.PHONE_NUM.getName(), userMember.getPhoneNumber());
        MockCookie isWorker = new MockCookie(CookieName.IS_WORKER.getName(), Integer.toString(userMember.getIsWorker()));
        MockCookie sessionId = new MockCookie(CookieName.SESSION_ID.getName(), userMember.getSessionId());
        MockCookie isAdmin = new MockCookie(CookieName.IS_ADMIN.getName(), Integer.toString(userMember.getIsAdmin()));
        MockCookie isAutoLogin = new MockCookie(CookieName.IS_AUTO_LOGIN.getName(), "false");

        // request
        ResultActions result = mockMvc.perform(
                RestDocumentationRequestBuilders.get("/api/attendance/date/{date}","2019-13")
                        .cookie(id)
                        .cookie(name)
                        .cookie(phoneNum)
                        .cookie(isWorker)
                        .cookie(sessionId)
                        .cookie(isAdmin)
                        .cookie(isAutoLogin)
        );
        // result
        result.andExpect(status().isAccepted())
                .andDo(print());
    }

    @Test
    @Transactional
    @TestDescription("특정일 참가 내역 조회 에러(파라미터 오류4)")
    public void getAllAttendanceList_badRequest_invalidParameter4() throws Exception {

        MockCookie id = new MockCookie(CookieName.ID.getName(), userMember.getId());
        MockCookie name = new MockCookie(CookieName.NAME.getName(), userMember.getName());
        MockCookie phoneNum = new MockCookie(CookieName.PHONE_NUM.getName(), userMember.getPhoneNumber());
        MockCookie isWorker = new MockCookie(CookieName.IS_WORKER.getName(), Integer.toString(userMember.getIsWorker()));
        MockCookie sessionId = new MockCookie(CookieName.SESSION_ID.getName(), userMember.getSessionId());
        MockCookie isAdmin = new MockCookie(CookieName.IS_ADMIN.getName(), Integer.toString(userMember.getIsAdmin()));
        MockCookie isAutoLogin = new MockCookie(CookieName.IS_AUTO_LOGIN.getName(), "false");

        // request
        ResultActions result = mockMvc.perform(
                RestDocumentationRequestBuilders.get("/api/attendance/date/{date}","2019-13-00")
                        .cookie(id)
                        .cookie(name)
                        .cookie(phoneNum)
                        .cookie(isWorker)
                        .cookie(sessionId)
                        .cookie(isAdmin)
                        .cookie(isAutoLogin)
        );
        // result
        result.andExpect(status().isAccepted())
                .andDo(print());
    }
    @Test
    @Transactional
    @TestDescription("참가 데이터 생성")
    public void createAttendance() throws Exception {
        MockCookie id = new MockCookie(CookieName.ID.getName(), userMember.getId());
        MockCookie name = new MockCookie(CookieName.NAME.getName(), userMember.getName());
        MockCookie phoneNum = new MockCookie(CookieName.PHONE_NUM.getName(), userMember.getPhoneNumber());
        MockCookie isWorker = new MockCookie(CookieName.IS_WORKER.getName(), Integer.toString(userMember.getIsWorker()));
        MockCookie sessionId = new MockCookie(CookieName.SESSION_ID.getName(), userMember.getSessionId());
        MockCookie isAdmin = new MockCookie(CookieName.IS_ADMIN.getName(), Integer.toString(userMember.getIsAdmin()));
        MockCookie isAutoLogin = new MockCookie(CookieName.IS_AUTO_LOGIN.getName(), "false");

        AttendanceDto.Request request = AttendanceDto.Request.builder()
                .availableDate("2019-11-10")
                .availableTime("1,2")
                .build();


        // request
        ResultActions result = mockMvc.perform(
                RestDocumentationRequestBuilders.post("/api/attendance")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaTypes.HAL_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .cookie(id)
                        .cookie(name)
                        .cookie(phoneNum)
                        .cookie(isWorker)
                        .cookie(sessionId)
                        .cookie(isAdmin)
                        .cookie(isAutoLogin)
        );
        // result
        result.andExpect(status().isCreated())
                .andDo(print());
    }
}
