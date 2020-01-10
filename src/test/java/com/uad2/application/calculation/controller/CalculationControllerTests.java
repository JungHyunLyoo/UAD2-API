package com.uad2.application.calculation.controller;
/*
 * @USER JungHyun
 * @DATE 2019-10-27
 * @DESCRIPTION
 */

import com.uad2.application.BaseControllerTest;
import com.uad2.application.common.TestDescription;
import com.uad2.application.common.enumData.CookieName;
import org.junit.Test;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.mock.web.MockCookie;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import javax.transaction.Transactional;

import static com.uad2.application.api.document.utils.DocumentFormatGenerator.getDateFormat;
import static com.uad2.application.api.document.utils.DocumentFormatGenerator.getDateTimeFormat;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureRestDocs(outputDir = "target/generated-snippets/calculation")
public class CalculationControllerTests  extends BaseControllerTest {
    @Test
    @Transactional
    @TestDescription("전체 정산 데이터 내역 조회")
    public void getAllCalculations() throws Exception {

        MockCookie id = new MockCookie(CookieName.ID.getName(), userMember.getId());
        MockCookie name = new MockCookie(CookieName.NAME.getName(), userMember.getName());
        MockCookie phoneNum = new MockCookie(CookieName.PHONE_NUM.getName(), userMember.getPhoneNumber());
        MockCookie isWorker = new MockCookie(CookieName.IS_WORKER.getName(), Integer.toString(userMember.getIsWorker()));
        MockCookie sessionId = new MockCookie(CookieName.SESSION_ID.getName(), userMember.getSessionId());
        MockCookie isAdmin = new MockCookie(CookieName.IS_ADMIN.getName(), Integer.toString(userMember.getIsAdmin()));
        MockCookie isAutoLogin = new MockCookie(CookieName.IS_AUTO_LOGIN.getName(), "false");

        // request
        ResultActions result = mockMvc.perform(
                RestDocumentationRequestBuilders.get("/api/calculation/year/{year}/month/{month}/memberSeq/{memberSeq}",2019,10,135)
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
                .andDo(print())
                .andDo(document("getAllCalculations",
                        pathParameters(
                                parameterWithName("year").description("졍산 연도"),
                                parameterWithName("month").description("정산 월"),
                                parameterWithName("memberSeq").description("회원 인덱스")
                        ),
                        responseFields(
                                subsectionWithPath("calculationList").description("정산 매칭 데이터 리스트"),
                                subsectionWithPath("count").description("카운팅"),
                                subsectionWithPath("totalCount").description("누적 카운팅"),
                                fieldWithPath("calculationList[].seq").type(JsonFieldType.NUMBER).description("회원 인덱스"),
                                fieldWithPath("calculationList[].matching").type(JsonFieldType.OBJECT).description("매칭 정보"),
                                fieldWithPath("calculationList[].price").type(JsonFieldType.NUMBER).description("회비"),
                                fieldWithPath("calculationList[].content").type(JsonFieldType.STRING).description("설명"),
                                fieldWithPath("calculationList[].kind").type(JsonFieldType.NUMBER).description("종류"),
                                fieldWithPath("calculationList[].calculationDate").type(JsonFieldType.STRING).attributes(getDateFormat()).description("정산일"),
                                fieldWithPath("calculationList[].attendCnt").type(JsonFieldType.NUMBER).description("참가 회원 카운팅"),
                                fieldWithPath("calculationList[].createAt").type(JsonFieldType.STRING).attributes(getDateTimeFormat()).description("생성일"),
                                fieldWithPath("calculationList[].updateAt").type(JsonFieldType.STRING).attributes(getDateTimeFormat()).description("수정일")
                        )
                ));
    }

    @Test
    @Transactional
    @TestDescription("전체 정산 데이터 내역 조회 에러(로그인 x)")
    public void getAllCalculations_badRequest_noAuth() throws Exception {

        // request
        ResultActions result = mockMvc.perform(
                RestDocumentationRequestBuilders.get("/api/calculation/year/{year}/month/{month}/memberSeq/{memberSeq}",2019,10,135)
        );
        // result
        result.andExpect(status().isBadRequest())
                .andDo(print());
    }
    @Test
    @Transactional
    @TestDescription("전체 정산 데이터 내역 조회 에러(파라미터 부재)")
    public void getAllCalculations_badRequest_noPathVariable() throws Exception {
        MockCookie id = new MockCookie(CookieName.ID.getName(), userMember.getId());
        MockCookie name = new MockCookie(CookieName.NAME.getName(), userMember.getName());
        MockCookie phoneNum = new MockCookie(CookieName.PHONE_NUM.getName(), userMember.getPhoneNumber());
        MockCookie isWorker = new MockCookie(CookieName.IS_WORKER.getName(), Integer.toString(userMember.getIsWorker()));
        MockCookie sessionId = new MockCookie(CookieName.SESSION_ID.getName(), userMember.getSessionId());
        MockCookie isAdmin = new MockCookie(CookieName.IS_ADMIN.getName(), Integer.toString(userMember.getIsAdmin()));
        MockCookie isAutoLogin = new MockCookie(CookieName.IS_AUTO_LOGIN.getName(), "false");

        // request
        ResultActions result = mockMvc.perform(
                RestDocumentationRequestBuilders.get("/api/calculation/year/{year}/month/{month}/memberSeq",2019,10)
                        .cookie(id)
                        .cookie(name)
                        .cookie(phoneNum)
                        .cookie(isWorker)
                        .cookie(sessionId)
                        .cookie(isAdmin)
                        .cookie(isAutoLogin)
        );
        // result
        result.andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @Transactional
    @TestDescription("전체 정산 데이터 내역 조회 에러(다른 회원의 내역 조회)")
    public void getAllCalculations_badRequest_requestOtherMemberInfo() throws Exception {


        MockCookie id = new MockCookie(CookieName.ID.getName(), userMember.getId());
        MockCookie name = new MockCookie(CookieName.NAME.getName(), userMember.getName());
        MockCookie phoneNum = new MockCookie(CookieName.PHONE_NUM.getName(), userMember.getPhoneNumber());
        MockCookie isWorker = new MockCookie(CookieName.IS_WORKER.getName(), Integer.toString(userMember.getIsWorker()));
        MockCookie sessionId = new MockCookie(CookieName.SESSION_ID.getName(), userMember.getSessionId());
        MockCookie isAdmin = new MockCookie(CookieName.IS_ADMIN.getName(), Integer.toString(userMember.getIsAdmin()));
        MockCookie isAutoLogin = new MockCookie(CookieName.IS_AUTO_LOGIN.getName(), "false");

        // request
        ResultActions result = mockMvc.perform(
                RestDocumentationRequestBuilders.get("/api/calculation/year/{year}/month/{month}/memberSeq/{memberSeq}",2019,10,134)
                        .cookie(id)
                        .cookie(name)
                        .cookie(phoneNum)
                        .cookie(isWorker)
                        .cookie(sessionId)
                        .cookie(isAdmin)
                        .cookie(isAutoLogin)
        );
        // result
        result.andExpect(status().isBadRequest())
                .andDo(print());
    }

    //정산 post 테스트케이스 작성 필요
}
