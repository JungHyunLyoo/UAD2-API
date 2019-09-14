package com.uad2.application.member.controller;

/*
 * @USER JungHyun
 * @DATE 2019-09-09
 */

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uad2.application.RestDocsConfiguration;
import com.uad2.application.common.TestDescription;
import com.uad2.application.member.entity.MemberInsertDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.uad2.application.api.document.utils.DocumentFormatGenerator.getDateFormat;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


//@RunWith(JUnitParamsRunner.class)
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(outputDir = "target/generated-snippets/sample")
@Import(RestDocsConfiguration.class)
public class MemberControllerTests {
    @Autowired
    MockMvc mockMvc;// 웹서버를 띄우진 않지만 디스패처서블릿을 사용

    @Autowired
    ObjectMapper objectMapper;//mappingJackson이 있으면 자동 등록

    @Test
    @TestDescription("전체 회원 조회")
    public void getAllMembers() throws Exception {
        // request
        ResultActions result = mockMvc.perform(
                RestDocumentationRequestBuilders.get("/api/member")
                        .accept(MediaTypes.HAL_JSON)
        );

        // result
        result.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("memberList").exists())
                .andDo(document("getAllMembers",//조각 모음 폴더의 이름
                        links(
                                linkWithRel("profile").description("restDoc link")
                        ),
                        responseFields(
                                subsectionWithPath("memberList").description("전체 회원 데이터 리스트"),
                                subsectionWithPath("_links").description("링크")
                        )
                ));
        /*
        result.andExpect(status().isOk())
                .andExpect(jsonPath("_embedded.memberList[0]").exists())
                .andDo(document("getAll",//조각 모음 폴더의 이름
                        links(  // _links path 하위
                                linkWithRel("first").description("첫 페이지"),
                                linkWithRel("self").description("현재 페이지"),
                                linkWithRel("last").description("이전 페이지"),
                                linkWithRel("next").description("다음 페이지")
                        ),/*
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("accept header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header")
                        ),
                        responseFields(
                                subsectionWithPath("_embedded.memberList").description("전체 회원 데이터 리스트"),
                                subsectionWithPath("_links").description("페이징 링크"),
                                subsectionWithPath("page").description("페이징 관련 데이터")
                        )
                ));*/
    }

    @Test
    @TestDescription("멤버 개별 조회 by id")
    public void getMemberById() throws Exception {
        // request
        ResultActions result = mockMvc.perform(
                RestDocumentationRequestBuilders.get("/api/member/id/{id}", "dkcmsa")
                        .accept(MediaTypes.HAL_JSON)
        );

        // result
        result.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("member").exists())
                .andDo(document("getMemberById",
                        pathParameters(
                                parameterWithName("id").description("아이디")
                        ),
                        links(
                                linkWithRel("profile").description("restDoc link")
                        ),
                        responseFields(
                                subsectionWithPath("member").description("회원 데이터"),
                                subsectionWithPath("_links").description("링크")
                        )
                ));

/*
        responseFields(
                getResponseFileds()
                        .stream()
                        .map(FieldDescriptor::optional) // response body 필수값 여부 우선 optional 처리
                        .collect(Collectors.toList())
        )*/
    }
    @Test
    @TestDescription("개별 회원 조회 에러(매개변수 미기입)")
    public void getMemberById_badRequest_emptyParameter() throws Exception {
        // request
        ResultActions result = mockMvc.perform(
                RestDocumentationRequestBuilders.get("/api/member/id/{id}", "")
                        .accept(MediaTypes.HAL_JSON)
        );

        // result
        result.andExpect(status().isNotFound())
                .andDo(print())
                .andDo(document("getMemberById_badRequest_emptyParameter",
                        pathParameters(
                                parameterWithName("id").description("아이디")
                        )));
    }
    @Test
    @Transactional
    @TestDescription("회원 데이터 생성")
    public void createMember() throws Exception {
        MemberInsertDto member = MemberInsertDto.builder()
                .id("testId")
                .pwd("testPwd")
                .name("testName")
                .birthDay(new Date())
                .studentId(11)
                .isWorker(0)
                .phoneNumber("01234567890")
                .build();

        // request
        ResultActions result = mockMvc.perform(
                RestDocumentationRequestBuilders.post("/api/member")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaTypes.HAL_JSON)
                        .content(objectMapper.writeValueAsString(member))
        );

        // result
        result.andExpect(status().isCreated())
                .andDo(print())
                .andExpect(jsonPath("member").exists())
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_UTF8_VALUE))
                .andDo(document("createMember",
                        requestFields(
                                fieldWithPath("id").type(JsonFieldType.STRING).description("아이디"),
                                fieldWithPath("pwd").type(JsonFieldType.STRING).description("비밀번호"),
                                fieldWithPath("name").type(JsonFieldType.STRING).description("이름"),
                                fieldWithPath("birthDay").type(JsonFieldType.STRING).description("생일"),
                                fieldWithPath("studentId").type(JsonFieldType.NUMBER).description("학번"),
                                fieldWithPath("isWorker").type(JsonFieldType.NUMBER).description("직장인 여부"),
                                fieldWithPath("phoneNumber").type(JsonFieldType.STRING).description("핸드폰 번호")
                        ),
                        responseFields(
                                subsectionWithPath("member").description("회원 데이터"),
                                subsectionWithPath("_links").description("링크")
                        )
/*
                        responseFields(//relaxedResponseFields( 정확한 문서를 만들지 못 한다는 단점이 있음
                                getResponseFileds()
                                        .stream()
                                        .map(FieldDescriptor::optional) // response body 필수값 여부 우선 optional 처리
                                        .collect(Collectors.toList())
                        )*/
                ));
    }

    @Test
    public void createMember_badRequest() throws Exception {
        /*
        Member member = Member.builder()
                .id("testId")
                .pwd("testPwd")
                .name("testName").build();

        mockMvc.perform(post("/api/member")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(member)))
                .andDo(print())
                .andExpect(status().isBadRequest());*/
    }

    @Test
    public void createMember_badRequest_emptyInput() throws Exception {/*
        MemberExternalDto memberExternalDto = MemberExternalDto.builder().build();

        mockMvc.perform(post("/api/member")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsBytes(memberExternalDto)))
                .andDo(print())
                .andExpect(status().isBadRequest());*/
    }

    @Test
    public void createMember_badRequest_wrongInput() throws Exception {/*
        MemberExternalDto memberExternalDto = MemberExternalDto.builder().build();

        mockMvc.perform(post("/api/member")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsBytes(memberExternalDto))
        ).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].objectName").exists());*/
    }

    private List<FieldDescriptor> getResponseFileds() {
        return Arrays.asList(
                fieldWithPath("seq").type(JsonFieldType.NUMBER).description("테이블 인덱스"),
                fieldWithPath("id").type(JsonFieldType.STRING).description("계정 아이디"),
                fieldWithPath("pwd").type(JsonFieldType.STRING).description("계정 패스워드"),
                fieldWithPath("name").type(JsonFieldType.STRING).description("이름"),
                fieldWithPath("phoneNumber").type(JsonFieldType.STRING).description("핸드폰 번호"),
                fieldWithPath("studentId").type(JsonFieldType.NUMBER).description("학번"),
                fieldWithPath("birthDay").type(JsonFieldType.STRING).description("생년월일").attributes(getDateFormat()),
                fieldWithPath("attdCnt").type(JsonFieldType.NUMBER).description("참석 횟수"),
                fieldWithPath("profileImg").type(JsonFieldType.STRING).description("프로필 이미지").optional(),
                fieldWithPath("sessionId").type(JsonFieldType.STRING).description("세션 고유번호").optional(),
                fieldWithPath("sessionLimit").type(JsonFieldType.STRING).description("세션 만료시점").optional().attributes(getDateFormat()),
                fieldWithPath("createdAt").type(JsonFieldType.STRING).description("계정 생성일").attributes(getDateFormat()),
                fieldWithPath("updatedAt").type(JsonFieldType.STRING).description("계정 수정일").attributes(getDateFormat()),
                fieldWithPath("admin").type(JsonFieldType.BOOLEAN).description("관리자 여부"),
                fieldWithPath("worker").type(JsonFieldType.BOOLEAN).description("직장인 여부"),
                fieldWithPath("benefit").type(JsonFieldType.BOOLEAN).description("")
        );
    }
    /*
    private Object[] paramsForTestFree(){
        return new Object[]{
                new Object[] {0},
                new Object[] {0}
        };
    }
    @Test
    @Parameters(method = "paramsForTestFree")
    public void testTest(int n1){
        Assert.assertEquals(n1,0);
    }*/
}
