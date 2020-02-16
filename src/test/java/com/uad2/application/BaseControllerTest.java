package com.uad2.application;
/*
 * @USER JungHyun
 * @DATE 2019-09-15
 * @DESCRIPTION 컨트롤러 테스트용 base 클래스
 */

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uad2.application.common.enumData.CookieName;
import com.uad2.application.member.dto.MemberDto;
import com.uad2.application.member.entity.Member;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockCookie;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import javax.servlet.http.Cookie;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.TimeZone;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Import(RestDocsConfiguration.class)
@Ignore
public class BaseControllerTest {

    @Autowired
    protected MockMvc mockMvc;// 웹서버를 띄우진 않지만 디스패처서블릿을 사용

    @Autowired
    protected ObjectMapper objectMapper;

    @Before
    public void setUp() {
    }

    protected ResultActions execLogin(String id, String pwd, boolean isAutoLogin) throws Exception{
        MemberDto.LoginRequest loginRequest = MemberDto.LoginRequest.builder()
                .id(id)
                .pwd(pwd)
                .isAutoLogin(isAutoLogin)
                .build();

        return mockMvc.perform(
                this.postRequest("/api/member/login",loginRequest)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaTypes.HAL_JSON)
        );
    }
    protected MockCookie[] convertCookieToMockCookie(Cookie[] autoLoginCookie) {
        int length = autoLoginCookie.length;
        MockCookie[] cookieList = new MockCookie[length];
        for (int i=0;i<length;i++) {
            cookieList[i] = new MockCookie(autoLoginCookie[i].getName(),autoLoginCookie[i].getValue());
        }
        return cookieList;
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
