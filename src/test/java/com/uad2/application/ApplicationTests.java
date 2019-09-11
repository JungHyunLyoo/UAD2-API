package com.uad2.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uad2.application.common.TestDescription;
import com.uad2.application.member.dto.MemberDto;
import com.uad2.application.member.entity.Member;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.uad2.application.api.document.utils.DocumentFormatGenerator.getDateFormat;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(outputDir = "target/generated-snippets/sample")
// @WebMvcTest   //웹과 관련된 빈만 등록
public class ApplicationTests {

    @Autowired
    MockMvc mockMvc;    // 웹서버를 띄우진 않지만 디스패처서블릿을 사용

    // mappingJackson이 있으면 자동 등록
    @Autowired
    ObjectMapper objectMapper;

    @Test
    @TestDescription("전체 회원 조회")
    public void getAllMembers() throws Exception {
        // request
        ResultActions result = mockMvc.perform(
                get("/api/member")
        );

        // result
        result.andExpect(status().isOk())
                .andExpect(jsonPath("_embedded.memberList[0]").exists())
                .andDo(document("getAll",
                        preprocessResponse(prettyPrint()),
                        links(  // _links path 하위
                                linkWithRel("first").description("첫 페이지"),
                                linkWithRel("self").description("현재 페이지"),
                                linkWithRel("last").description("이전 페이지"),
                                linkWithRel("next").description("다음 페이지")
                        ),
                        responseFields(
                                subsectionWithPath("_embedded.memberList").description("전체 회원 데이터 리스트"),
                                subsectionWithPath("_links").description("페이징 링크"),
                                subsectionWithPath("page").description("페이징 관련 데이터")
                        )
                ));
    }

    @Test
    @TestDescription("개별 회원 조회")
    public void getMember() throws Exception {
        // request
        ResultActions result = mockMvc.perform(
                RestDocumentationRequestBuilders.get("/api/member/{name}", "testName")
        );

        // result
        result.andExpect(status().isOk())
                .andDo(document("getMember",
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("name").description("이름")
                        ),
                        responseFields(
                                getResponseFileds()
                                        .stream()
                                        .map(FieldDescriptor::optional) // response body 필수값 여부 우선 optional 처리
                                        .collect(Collectors.toList())
                        )
                ));
    }

    @Test
    @TestDescription("정상 실행")
    public void createMember() throws Exception {
        MemberDto member = MemberDto.builder()
                .id("testId")
                .pwd("testPwd").build();

        // request
        ResultActions result = mockMvc.perform(post("/api/member")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(member))
        );

        // result
        result.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("seq").exists())
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_UTF8_VALUE))
                .andDo(document("createMember",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("id").type(JsonFieldType.STRING).description("계정 아이디"),
                                fieldWithPath("pwd").type(JsonFieldType.STRING).description("계정 패스워드")
                        ),
                        responseFields(
                                getResponseFileds()
                                        .stream()
                                        .map(FieldDescriptor::optional) // response body 필수값 여부 우선 optional 처리
                                        .collect(Collectors.toList())
                        )
                ));
    }

    @Test
    public void createMember_badRequest() throws Exception {
        Member member = Member.builder()
                .id("testId")
                .pwd("testPwd")
                .name("testName").build();

        mockMvc.perform(post("/api/member")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(member)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createMember_badRequest_emptyInput() throws Exception {
        MemberDto memberDto = MemberDto.builder().build();

        mockMvc.perform(post("/api/member")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsBytes(memberDto)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createMember_badRequest_wrongInput() throws Exception {
        MemberDto memberDto = MemberDto.builder().build();

        mockMvc.perform(post("/api/member")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsBytes(memberDto))
        ).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].objectName").exists());
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

}
