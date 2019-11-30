package com.uad2.application.matching.controller;
/*
 * @USER JungHyun
 * @DATE 2019-11-17
 * @DESCRIPTION
 */

import com.uad2.application.BaseControllerTest;
import com.uad2.application.common.TestDescription;
import com.uad2.application.common.enumData.CookieName;
import org.junit.Test;
import org.springframework.mock.web.MockCookie;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.ResultActions;

import javax.transaction.Transactional;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class MatchingControllerTests extends BaseControllerTest {
    @Test
    @Transactional
    @TestDescription("월별 매칭 내역 조회")
    public void getMatching() throws Exception {

        MockCookie id = new MockCookie(CookieName.ID.getName(), userMember.getId());
        MockCookie name = new MockCookie(CookieName.NAME.getName(), userMember.getName());
        MockCookie phoneNum = new MockCookie(CookieName.PHONE_NUM.getName(), userMember.getPhoneNumber());
        MockCookie isWorker = new MockCookie(CookieName.IS_WORKER.getName(), Integer.toString(userMember.getIsWorker()));
        MockCookie sessionId = new MockCookie(CookieName.SESSION_ID.getName(), userMember.getSessionId());
        MockCookie isAdmin = new MockCookie(CookieName.IS_ADMIN.getName(), Integer.toString(userMember.getIsAdmin()));
        MockCookie isAutoLogin = new MockCookie(CookieName.IS_AUTO_LOGIN.getName(), "false");

        // request
        ResultActions result = mockMvc.perform(
                RestDocumentationRequestBuilders.get("/api/matching/date/{date}","2019-11")
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
}
