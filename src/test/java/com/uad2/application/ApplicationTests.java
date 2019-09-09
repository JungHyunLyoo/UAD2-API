package com.uad2.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uad2.application.common.TestDescription;
import com.uad2.application.member.dto.MemberDto;
import com.uad2.application.member.entity.Member;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
//@WebMvcTest//웹과 관련된 빈만 등록
public class ApplicationTests {
    @Autowired
    MockMvc mockMvc;//웹서버를 띄우진 않지만 디스패처서블릿을 사용

    //mappingJackson이 있으면 자동 등록
    @Autowired
    ObjectMapper objectMapper;

    @Test
    @TestDescription("전체 회원 조회")
    public void getAllMembers() throws Exception{
        mockMvc.perform(get("/api/member")
        ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("_embedded.memberList[0]").exists());
    }

    @Test
    @TestDescription("개별 회원 조회")
    public void getMember() throws Exception{


        mockMvc.perform(get("/api/member/{name}","유정현")
        ).andDo(print())
                .andExpect(status().isOk());
                //.andExpect(jsonPath("_embedded.memberList[0]").exists());
    }

    @Test
    @TestDescription("정상 실행")
    public void createMember() throws Exception{
        MemberDto member = MemberDto.builder()
                .id("testId")
                .pwd("testPwd").build();
        mockMvc.perform(post("/api/member")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(member))
        ).andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("seq").exists())
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE,MediaTypes.HAL_JSON_UTF8_VALUE));

    }

    @Test
    public void createMember_badRequest() throws Exception{
        Member member = Member.builder()
                .id("testId")
                .pwd("testPwd")
                .name("testName").build();

        mockMvc.perform(post("/api/member")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(member))
        ).andDo(print())
                .andExpect(status().isBadRequest());
    }


    @Test
    public void createMember_badRequest_emptyInput() throws Exception{
        MemberDto memberDto = MemberDto.builder().build();

        mockMvc.perform(post("/api/member")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsBytes(memberDto)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
    @Test
    public void createMember_badRequest_wrongInput() throws Exception{
        MemberDto memberDto = MemberDto.builder().build();

        mockMvc.perform(post("/api/member")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(objectMapper.writeValueAsBytes(memberDto))
        ).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].objectName").exists());
    }
}
