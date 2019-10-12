package com.uad2.application.config;

/*
 * @USER JungHyun
 * @DATE 2019-09-22
 * @DESCRIPTION 환경설정
 */

import com.uad2.application.common.interceptor.CommonInterceptor;
import com.uad2.application.common.interceptor.LoginInterceptor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private CommonInterceptor commonInterceptor;

    @Autowired
    private LoginInterceptor loginInterceptor;

    @Bean
    public ModelMapper modelMapper() {
        //DTO와 같은 클래스들의 서로간 변환을 담당
        return new ModelMapper();
    }

    /**
     * 인터셉터 등록
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        /*
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/api/member/login");  // 로그인 인터셉터에서 로그인 URI 처리
         */
        registry.addInterceptor(commonInterceptor)
                .excludePathPatterns("/api/member/login");  // 핸들러 인터셉터에서 로그인 URI 제외

        WebMvcConfigurer.super.addInterceptors(registry);
    }

}
