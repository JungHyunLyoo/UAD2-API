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
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockCookie;
import org.springframework.mock.web.MockHttpServletResponse;
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

        MockHttpServletResponse response = super.execLogin("testUser","testUser",false).andReturn().getResponse();

        MockCookie[] cookieList = super.convertCookieToMockCookie(response.getCookies());

        String[] paramList = new String[]{"2019","10"};

        // request
        ResultActions result = mockMvc.perform(
                super.getRequest("/api/calculation/year/{year}/month/{month}",paramList, cookieList)
                        .accept(MediaType.APPLICATION_JSON_UTF8)
        );

        // result
        result.andExpect(status().isOk())
                .andDo(print())
                .andDo(document("getAllCalculations",
                        pathParameters(
                                parameterWithName("year").description("졍산 연도"),
                                parameterWithName("month").description("정산 월")
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
                RestDocumentationRequestBuilders.get("/api/calculation/year/{year}/month/{month}",2019,135)
        );
        // result
        result.andExpect(status().isBadRequest())
                .andDo(print());
    }
    @Test
    @Transactional
    @TestDescription("전체 정산 데이터 내역 조회 에러(파라미터 부재)")
    public void getAllCalculations_badRequest_noPathVariable() throws Exception {
        MockHttpServletResponse response = super.execLogin("testUser","testUser",false).andReturn().getResponse();

        MockCookie[] cookieList = super.convertCookieToMockCookie(response.getCookies());

        String[] paramList = new String[]{"2019","10"};

        // request
        ResultActions result = mockMvc.perform(
                super.getRequest("/api/calculation/year/{year}/month/{month}/memberSeq",paramList, cookieList)
                        .accept(MediaType.APPLICATION_JSON_UTF8)
        );
        // result
        result.andExpect(status().isNotFound())
                .andDo(print());
    }

    //정산 post 테스트케이스 작성 필요
}
