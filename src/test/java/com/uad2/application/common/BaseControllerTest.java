package com.uad2.application.common;
/*
 * @USER JungHyun
 * @DATE 2019-09-15
 * @DESCRIPTION 컨트롤러 테스트용 base 클래스
 */

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uad2.application.RestDocsConfiguration;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(outputDir = "target/generated-snippets/sample")
@Import(RestDocsConfiguration.class)
@Ignore
public class BaseControllerTest {
    @Autowired
    protected MockMvc mockMvc;// 웹서버를 띄우진 않지만 디스패처서블릿을 사용

    @Autowired
    protected ObjectMapper objectMapper;//mappingJackson이 있으면 자동 등록

}
