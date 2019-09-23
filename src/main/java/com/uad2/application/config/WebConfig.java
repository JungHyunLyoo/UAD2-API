package com.uad2.application.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/*
 * @USER JungHyun
 * @DATE 2019-09-22
 * @DESCRIPTION 환경설정
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Bean
    public ModelMapper modelMapper(){
        //DTO와 같은 클래스들의 서로간 변환을 담당
        return new ModelMapper();
    }
}
