package com.uad2.application;
/*
 * @USER JungHyun
 * @DATE 2019-09-15
 * @DESCRIPTION 컨트롤러 테스트용 base 클래스
 */

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uad2.application.common.enumData.CookieName;
import com.uad2.application.member.entity.Member;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockCookie;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.TimeZone;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Import(RestDocsConfiguration.class)
@Ignore
public class BaseControllerTest {
    protected static final String AUTOLOGIN_FALSE = "false";
    protected static final String AUTOLOGIN_TRUE = "true";

    protected MockHttpSession session;
    protected Member adminMember;
    protected Member userMember;

    @Autowired
    protected MockMvc mockMvc;// 웹서버를 띄우진 않지만 디스패처서블릿을 사용

    @Autowired
    protected ObjectMapper objectMapper;//mappingJackson이 있으면 자동 등록


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
                .sessionId("CC1A4A921C8A4CB31B916E31A8F1C9D0")
                .sessionLimit(calendar.getTime())
                .isAdmin(0)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }
    protected MockCookie[] getUserMemberCookieList(String isAutoLogin){
        return getMemberCookieList(userMember,isAutoLogin);
    }
    protected MockCookie[] getAdminMemberCookieList(String isAutoLogin){
        return getMemberCookieList(adminMember,isAutoLogin);
    }
    protected MockCookie[] getMemberCookieList(Member member,String isAutoLogin){
        MockCookie[] mockCookieList = new MockCookie[7];
        mockCookieList[0] = new MockCookie(CookieName.ID.getName(), member.getId());
        mockCookieList[1] = new MockCookie(CookieName.NAME.getName(), member.getName());
        mockCookieList[2] = new MockCookie(CookieName.PHONE_NUM.getName(), member.getPhoneNumber());
        mockCookieList[3] = new MockCookie(CookieName.IS_WORKER.getName(), Integer.toString(member.getIsWorker()));
        mockCookieList[4] = new MockCookie(CookieName.SESSION_ID.getName(), member.getSessionId());
        mockCookieList[5] = new MockCookie(CookieName.IS_ADMIN.getName(), Integer.toString(member.getIsAdmin()));
        mockCookieList[6] = new MockCookie(CookieName.IS_AUTO_LOGIN.getName(), isAutoLogin);
        return mockCookieList;
    }
    protected MockHttpServletRequestBuilder getRequest(String url){
        return RestDocumentationRequestBuilders.get(url);
    }
    protected MockHttpServletRequestBuilder getRequest(String url, Object[] paramList){
        return RestDocumentationRequestBuilders.get(url,paramList);
    }
    protected MockHttpServletRequestBuilder getRequest(String url, MockCookie[] mockCookieList){
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = RestDocumentationRequestBuilders.get(url);
        for (MockCookie mockCookie : mockCookieList) {
            mockHttpServletRequestBuilder.cookie(mockCookie);
        }
        return mockHttpServletRequestBuilder;
    }
    protected MockHttpServletRequestBuilder getRequest(String url, Object[] paramList, MockCookie[] mockCookieList){
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = RestDocumentationRequestBuilders.get(url,paramList);
        for (MockCookie mockCookie : mockCookieList) {
            mockHttpServletRequestBuilder.cookie(mockCookie);
        }
        return mockHttpServletRequestBuilder;
    }
    protected MockHttpServletRequestBuilder postRequest(String url,Object content) throws Exception{
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = RestDocumentationRequestBuilders.post(url);
        mockHttpServletRequestBuilder.content(objectMapper.writeValueAsString(content));
        return mockHttpServletRequestBuilder;
    }
    protected MockHttpServletRequestBuilder postRequest(String url,Object content, MockCookie[] mockCookieList) throws Exception{
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = RestDocumentationRequestBuilders.post(url);
        mockHttpServletRequestBuilder.content(objectMapper.writeValueAsString(content));
        for (MockCookie mockCookie : mockCookieList) {
            mockHttpServletRequestBuilder.cookie(mockCookie);
        }
        return mockHttpServletRequestBuilder;
    }
}
